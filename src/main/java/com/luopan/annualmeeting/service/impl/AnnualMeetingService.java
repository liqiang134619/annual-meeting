package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.AnnualMeetingStateVO;
import com.luopan.annualmeeting.service.IAnnualMeetingService;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnualMeetingService implements IAnnualMeetingService {

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public RespMsg setState(AnnualMeetingStateVO annualMeetingStateVO) {
    Date startTime = annualMeetingStateVO.getStartTime();
    if (startTime != null) {
      redisUtil.set(RedisKey.ANNUAL_MEETING_START_TIME, startTime);
    }
    Date endTime = annualMeetingStateVO.getEndTime();
    if (endTime != null) {
      redisUtil.set(RedisKey.ANNUAL_MEETING_END_TIME, endTime);
    }
    return ResultUtil.success();
  }
}
