package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonSpeakStatusVO;
import com.luopan.annualmeeting.entity.vo.PersonVO;
import com.luopan.annualmeeting.entity.vo.WeChatCodeVO;
import com.luopan.annualmeeting.entity.vo.WeChatSignInVO;
import com.luopan.annualmeeting.service.IPersonService;
import com.luopan.annualmeeting.util.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/person")
@Api(value = "人员", tags = "人员")
public class PersonController {

  @Autowired
  private IPersonService personService;

  @RequestMapping(value = "/signIn", method = RequestMethod.POST)
  public RespMsg signIn(@RequestBody PersonVO personVO) {
    log.info("***********签到***********");
    return personService.signIn(personVO);
  }

  @ApiOperation(value = "查看签到人员")
  @RequestMapping(value = "/findSignInPeople", method = RequestMethod.GET)
  public RespMsg findSignInPeople(HttpServletRequest request) {
    log.info("***********查看签到人员************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return personService.findSignInPeople(companyId);
  }

  @RequestMapping(value = "/auth", method = RequestMethod.POST)
  public RespMsg auth(@RequestBody WeChatSignInVO weChatSignInVO, HttpServletRequest request) {
    log.info("***********授权签到************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    weChatSignInVO.setCompanyId(companyId);
    return personService.auth(weChatSignInVO);
  }

  @RequestMapping(value = "/judgeSignIn", method = RequestMethod.POST)
  public RespMsg judgeSignIn(@RequestBody WeChatCodeVO weChatCodeVO, HttpServletRequest request) {
    log.info("***********判断是否签到************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    weChatCodeVO.setCompanyId(companyId);
    return personService.judgeSignIn(weChatCodeVO);
  }

  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public RespMsg search(@RequestBody PersonSearchVO personSearchVO, HttpServletRequest request) {
    log.info("***********搜索************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    personSearchVO.setCompanyId(companyId);
    return personService.search(personSearchVO);
  }

  @RequestMapping(value = "/banned", method = RequestMethod.POST)
  public RespMsg banned(@RequestBody PersonSpeakStatusVO personSpeakStatusVO) {
    log.info("***********禁言************");
    return personService.banned(personSpeakStatusVO);
  }

}
