package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonSpeakStatusVO;
import com.luopan.annualmeeting.entity.vo.PersonVO;
import com.luopan.annualmeeting.entity.vo.WeChatCodeVO;
import com.luopan.annualmeeting.entity.vo.WeChatSignInVO;
import com.luopan.annualmeeting.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/person")
public class PersonController {

  @Autowired
  private IPersonService personService;

  @RequestMapping(value = "/signIn", method = RequestMethod.POST)
  public RespMsg signIn(@RequestBody PersonVO personVO) {
    log.info("***********签到***********");
    return personService.signIn(personVO);
  }

  @RequestMapping(value = "/findSignInPeople", method = RequestMethod.GET)
  public RespMsg findSignInPeople() {
    log.info("***********查看签到人员************");
    return personService.findSignInPeople();
  }

  @RequestMapping(value = "/auth", method = RequestMethod.POST)
  public RespMsg auth(@RequestBody WeChatSignInVO weChatSignInVO) {
    log.info("***********授权签到************");
    return personService.auth(weChatSignInVO);
  }

  @RequestMapping(value = "/judgeSignIn", method = RequestMethod.POST)
  public RespMsg judgeSignIn(@RequestBody WeChatCodeVO weChatCodeVO) {
    log.info("***********判断是否签到************");
    return personService.judgeSignIn(weChatCodeVO);
  }

  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public RespMsg search(@RequestBody PersonSearchVO personSearchVO) {
    log.info("***********搜索************");
    return personService.search(personSearchVO);
  }

  @RequestMapping(value = "/banned", method = RequestMethod.POST)
  public RespMsg banned(@RequestBody PersonSpeakStatusVO personSpeakStatusVO) {
    log.info("***********禁言************");
    return personService.banned(personSpeakStatusVO);
  }

}
