package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.MessageCheckStatus;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.MessageDao;
import com.luopan.annualmeeting.entity.Message;
import com.luopan.annualmeeting.entity.vo.MessageCheckVO;
import com.luopan.annualmeeting.entity.vo.MessageManageVO;
import com.luopan.annualmeeting.entity.vo.MessageSearchVO;
import com.luopan.annualmeeting.entity.vo.MessageTopVO;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.Date;
import java.util.List;
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
  public List<MessageVO> findSendMessages(List<Long> ids) {
    return messageDao.findSendMessages(ids);
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
    if (MessageCheckStatus.PASS == messageCheckVO.getCheckStatus()) {
      redisUtil.lSet(RedisKey.CHECK_PASS_MESSAGES, messageCheckVO.getId());
    }
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
}
