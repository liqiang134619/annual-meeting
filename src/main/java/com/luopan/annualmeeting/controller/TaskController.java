package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.MessageTaskVO;
import com.luopan.annualmeeting.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

  @Autowired
  private ITaskService taskService;

  @RequestMapping(value = "/update", method = RequestMethod.POST)
  public RespMsg update(@RequestBody MessageTaskVO messageTaskVO) {
    log.info("*************修改留言墙任务配置*************");
    return taskService.updateTask(messageTaskVO);
  }

}
