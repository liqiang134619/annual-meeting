package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.MessageTaskVO;

public interface ITaskService {

  RespMsg updateTask(MessageTaskVO messageTaskVO);

}
