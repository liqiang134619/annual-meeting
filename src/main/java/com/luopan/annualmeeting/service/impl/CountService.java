package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.MessageDao;
import com.luopan.annualmeeting.dao.PersonDao;
import com.luopan.annualmeeting.entity.vo.CountVO;
import com.luopan.annualmeeting.service.ICountService;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import com.luopan.annualmeeting.util.Tools;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountService implements ICountService {

  @Autowired
  private PersonDao personDao;

  @Autowired
  private MessageDao messageDao;

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public RespMsg total() {
    long signNum = personDao.countAll();
    long messageNum = messageDao.countAll();
    int taskInterval = Tools
        .getInt(redisUtil.getString(RedisKey.MESSAGE_TASK_INTERVAL),
            Constant.MESSAGE_TASK_INTERVAL);
    int taskMessageNum = Tools
        .getInt(redisUtil.getString(RedisKey.MESSAGE_TASK_NUM), Constant.MESSAGE_TASK_NUM);
    int personVoteNum = Tools
        .getInt(redisUtil.getString(RedisKey.PERSON_VOTE_NUM), Constant.PERSON_VOTE_NUM);
    Date voteStartTime = (Date) redisUtil.get(RedisKey.VOTE_START_TIME);
    Date voteEndTime = (Date) redisUtil.get(RedisKey.VOTE_END_TIME);
    Date annualMeetingStartTime = (Date) redisUtil.get(RedisKey.ANNUAL_MEETING_START_TIME);
    Date annualMeetingEndTime = (Date) redisUtil.get(RedisKey.ANNUAL_MEETING_END_TIME);
    CountVO countVO = new CountVO();
    countVO.setVoteStartTime(voteStartTime);
    countVO.setVoteEndTime(voteEndTime);
    countVO.setTaskMessageNum(taskMessageNum);
    countVO.setTaskInterval(taskInterval);
    countVO.setSignNum(signNum);
    countVO.setPersonVoteNum(personVoteNum);
    countVO.setMessageNum(messageNum);
    countVO.setAnnualMeetingStartTime(annualMeetingStartTime);
    countVO.setAnnualMeetingEndTime(annualMeetingEndTime);
    return ResultUtil.success(countVO);
  }

}
