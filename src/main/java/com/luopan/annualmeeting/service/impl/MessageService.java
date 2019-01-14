package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.Status;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.MessageDao;
import com.luopan.annualmeeting.entity.Message;
import com.luopan.annualmeeting.entity.vo.LimitVO;
import com.luopan.annualmeeting.entity.vo.MessageCheckVO;
import com.luopan.annualmeeting.entity.vo.MessageManageVO;
import com.luopan.annualmeeting.entity.vo.MessageSearchVO;
import com.luopan.annualmeeting.entity.vo.MessageTopVO;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import com.luopan.annualmeeting.entity.vo.SelfMessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import com.luopan.annualmeeting.util.Tools;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageService implements IMessageService {

  @Autowired
  private MessageDao messageDao;

  @Autowired
  private RedisUtil redisUtil;

  @Transactional
  @Override
  public int insert(Message message) {
    return messageDao.insert(message);
  }

  @Override
  public List<MessageVO> findSendMessages(Long offset, Integer size) {
    LimitVO limitVO = new LimitVO();
    limitVO.setOffset(offset);
    limitVO.setSize(size);
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
  public RespMsg sendMessage(Long personId, String message) {
    if (redisUtil.sContain(RedisKey.BANNED_PERSON_ID, personId.toString())) {
      return ResultUtil.error(ErrCode.HAD_BANNED);
    }
    Message msg = new Message();
    msg.setMessage(message);
    msg.setPersonId(personId);
    msg.fillDefaultProperty();
    Integer checkState = Tools
        .getInt(redisUtil.getString(RedisKey.MESSAGE_CHECK_STATE), Status.DISABLE);
    // 默认消息不需要审核
    if (checkState == Status.DISABLE) {
      msg.setCheckTime(new Date());
      msg.setCheckStatus(Status.ENABLE);
    } else {
      msg.setCheckStatus(Status.DISABLE);
    }
    messageDao.insert(msg);
    return ResultUtil.success();
  }

}
