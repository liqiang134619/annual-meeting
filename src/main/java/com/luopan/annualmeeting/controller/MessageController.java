package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.MessageCheckVO;
import com.luopan.annualmeeting.entity.vo.MessageSearchVO;
import com.luopan.annualmeeting.entity.vo.MessageTopVO;
import com.luopan.annualmeeting.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

  @Autowired
  private IMessageService messageService;

  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public RespMsg search(@RequestBody MessageSearchVO messageSearchVO) {
    log.info("**********搜索留言**********");
    return messageService.search(messageSearchVO);
  }

  @RequestMapping(value = "/check", method = RequestMethod.POST)
  public RespMsg check(@RequestBody MessageCheckVO messageCheckVO) {
    log.info("**********审批留言**********");
    return messageService.check(messageCheckVO);
  }

  @RequestMapping(value = "/top", method = RequestMethod.POST)
  public RespMsg top(@RequestBody MessageTopVO messageTopVO) {
    log.info("**********置顶留言**********");
    return messageService.top(messageTopVO);
  }

}
