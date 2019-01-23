package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.QuartzJobName;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.Status;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.MessageDao;
import com.luopan.annualmeeting.entity.Message;
import com.luopan.annualmeeting.entity.vo.LimitVO;
import com.luopan.annualmeeting.entity.vo.MessageCheckVO;
import com.luopan.annualmeeting.entity.vo.MessageGetVO;
import com.luopan.annualmeeting.entity.vo.MessageManageVO;
import com.luopan.annualmeeting.entity.vo.MessageSearchVO;
import com.luopan.annualmeeting.entity.vo.MessageSendVO;
import com.luopan.annualmeeting.entity.vo.MessageTopVO;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import com.luopan.annualmeeting.entity.vo.SelfMessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.task.QuartzManager;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MessageService implements IMessageService {

  @Autowired
  private MessageDao messageDao;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private QuartzManager quartzManager;

  @Transactional
  @Override
  public int insert(Message message) {
    return messageDao.insert(message);
  }

  @Override
  public List<MessageVO> findSendMessages(Long offset, Integer size, Long companyId) {
    LimitVO limitVO = new LimitVO();
    limitVO.setOffset(offset);
    limitVO.setSize(size);
    limitVO.setCompanyId(companyId);
    return messageDao.findSendMessages(limitVO);
  }

  @Override
  public RespMsg search(MessageSearchVO messageSearchVO) {
    List<MessageManageVO> list = messageDao.search(messageSearchVO);
    return ResultUtil.success(list);
  }

  @Transactional
  @Override
  public RespMsg check(MessageCheckVO messageCheckVO) {
    Message message = messageDao.findById(messageCheckVO.getId());
    if (message == null) {
      return ResultUtil.error(ErrCode.MESSAGE_NOT_FOUND);
    }
    Message dbMsg = new Message();
    BeanUtils.copyProperties(messageCheckVO, dbMsg);
    dbMsg.setCheckTime(new Date());
    messageDao.updateSelective(dbMsg);
    return ResultUtil.success();
  }

  @Transactional
  @Override
  public RespMsg top(MessageTopVO messageTopVO) {
    Message message = messageDao.findById(messageTopVO.getId());
    if (message == null) {
      return ResultUtil.error(ErrCode.MESSAGE_NOT_FOUND);
    }
    Message dbMsg = new Message();
    BeanUtils.copyProperties(messageTopVO, dbMsg);
    dbMsg.setTopTime(new Date());
    messageDao.updateSelective(dbMsg);
    return ResultUtil.success();
  }

  @Override
  public List<SelfMessageVO> findSelfMessages(Long personId) {
    List<Message> list = messageDao.findSelfMessages(personId);
    List<SelfMessageVO> selfMessageVOS = Optional.ofNullable(list)
        .orElse(Collections.emptyList())
        .stream().map(message -> {
          SelfMessageVO selfMessageVO = new SelfMessageVO();
          BeanUtils.copyProperties(message, selfMessageVO);
          selfMessageVO.setPubTime(message.getCreateTime());
          return selfMessageVO;
        }).collect(Collectors.toList());
    return selfMessageVOS;
  }

  @Transactional
  @Override
  public RespMsg sendMessage(MessageSendVO messageSendVO) {
    Date now = new Date();
    Long personId = messageSendVO.getPersonId();
    String message = messageSendVO.getMessage();
    Long companyId = messageSendVO.getCompanyId();
    if (personId == null || StringUtils.isEmpty(message) || Constant.EMPTY_STR
        .equals(message.trim())) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    // 判断是否被禁言
    if (redisUtil.sContain(RedisKey.BANNED_PERSON_ID, personId)) {
      return ResultUtil.error(ErrCode.HAD_BANNED);
    }

    Message messagePO = BeanUtil.copyProperties(messageSendVO, Message.class);
    messagePO.fillDefaultProperty();
    messagePO.setCreateTime(now).setUpdateTime(now);

    Integer checkState = Optional.ofNullable(
        redisUtil.get(
            RedisKey.MESSAGE_CHECK_STATE + Constant.SPLITTER_COLON + companyId,
            Integer.class)).orElse(Constant.MESSAGE_CHECK_STATUS);
    if (checkState == Status.DISABLE) {
      messagePO.setCheckTime(now);
      messagePO.setCheckStatus(Status.ENABLE);
    } else {
      messagePO.setCheckStatus(Status.DISABLE);
    }
    messageDao.insert(messagePO);
    return ResultUtil.success();
  }

  @Override
  public RespMsg getMoreMessages(MessageGetVO messageGetVO) {
    String act = messageGetVO.getAct();
    if (!Constant.ACT_LOAD.equals(act) && !Constant.ACT_REFRESH.equals(act)
        || messageGetVO.getPersonId() == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }
    messageGetVO.setNum(Constant.ACT_MESSAGE_NUM);
    List<MessageVO> list;
    if (Constant.ACT_LOAD.equals(act)) {
      if (messageGetVO.getLastId() == null) {
        return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
      }
      list = messageDao.findLoadMessages(messageGetVO);
    } else {
      list = messageDao.findRefreshMessages(messageGetVO);
      if (messageGetVO.getLastId() != null && !BeanUtil.isEmpty(list)) {
        Collections.reverse(list);
      }
    }
    list = Optional.ofNullable(list).orElse(Collections.emptyList());
    return ResultUtil.success(list);
  }

  @Override
  public RespMsg pause(Long companyId) {
    Boolean success = quartzManager
        .pauseJob(QuartzJobName.MESSAGE + Constant.SPLITTER_COLON + companyId);
    if (!success) {
      return ResultUtil.error(ErrCode.TASK_PAUSE_ERROR);
    }
    return ResultUtil.success();
  }

  @Override
  public RespMsg change(Long companyId) {
    String jobName = QuartzJobName.MESSAGE + Constant.SPLITTER_COLON + companyId;
    Boolean eSuccess = quartzManager.executeOnce(jobName);
    if (!eSuccess) {
      return ResultUtil.error(ErrCode.MESSAGE_CHANGE_ERROR);
    }
    return ResultUtil.success();
  }

  @Override
  public RespMsg resume(Long companyId) {
    Boolean success = quartzManager
        .resumeJob(QuartzJobName.MESSAGE + Constant.SPLITTER_COLON + companyId);
    if (!success) {
      return ResultUtil.error(ErrCode.TASK_RESUME_ERROR);
    }
    return ResultUtil.success();
  }
}
