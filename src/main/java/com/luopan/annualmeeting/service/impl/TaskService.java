package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.MessageTaskVO;
import com.luopan.annualmeeting.service.ITaskService;
import com.luopan.annualmeeting.task.QuartzManager;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService implements ITaskService {

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private QuartzManager quartzScheduler;

  @Override
  public RespMsg updateTask(MessageTaskVO messageTaskVO) {
    Integer messageNum = messageTaskVO.getNum();
    if (messageNum != null && messageNum > 0) {
      redisUtil.setString(RedisKey.MESSAGE_TASK_NUM, messageNum.toString());
    }
    Integer interval = messageTaskVO.getInterval();
    if (interval != null && interval > 0) {
      redisUtil.setString(RedisKey.MESSAGE_TASK_INTERVAL, interval.toString());
      boolean success = quartzScheduler.modifyMessageJob(interval);
      if (!success) {
        return ResultUtil.error(ErrCode.TASK_MODIFY_INTERVAL_ERROR);
      }
    }
    return ResultUtil.success();
  }

}
