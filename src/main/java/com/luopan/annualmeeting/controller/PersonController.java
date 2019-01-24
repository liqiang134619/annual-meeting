package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PersonEntryVO;
import com.luopan.annualmeeting.entity.vo.PersonFaceSignInVO;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonSpeakStatusVO;
import com.luopan.annualmeeting.entity.vo.WeChatCodeVO;
import com.luopan.annualmeeting.service.IPersonService;
import com.luopan.annualmeeting.util.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

  @ApiOperation(value = "面部识别签到")
  @RequestMapping(value = "/faceSignIn", method = RequestMethod.POST)
  public RespMsg faceSignIn(@RequestBody PersonFaceSignInVO personFaceSignInVO) {
    log.info("***********面部识别签到***********");
    return personService.faceSignIn(personFaceSignInVO);
  }

  @ApiOperation(value = "查看签到人员")
  @RequestMapping(value = "/findSignInPeople", method = RequestMethod.GET)
  public RespMsg findSignInPeople(HttpServletRequest request) {
    log.info("***********查看签到人员************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return personService.findSignInPeople(companyId);
  }

  @ApiOperation(value = "判断是否签到")
  @RequestMapping(value = "/judgeSignIn", method = RequestMethod.POST)
  public RespMsg judgeSignIn(@RequestBody WeChatCodeVO weChatCodeVO, HttpServletRequest request) {
    log.info("***********判断是否签到************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    weChatCodeVO.setCompanyId(companyId);
    return personService.judgeSignIn(weChatCodeVO);
  }

  @ApiOperation(value = "签到人员搜索")
  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public RespMsg search(@RequestBody PersonSearchVO personSearchVO, HttpServletRequest request) {
    log.info("***********签到人员搜索************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    personSearchVO.setCompanyId(companyId);
    return personService.search(personSearchVO);
  }

  @ApiOperation(value = "禁言")
  @RequestMapping(value = "/banned", method = RequestMethod.POST)
  public RespMsg banned(@RequestBody PersonSpeakStatusVO personSpeakStatusVO) {
    log.info("***********禁言************");
    return personService.banned(personSpeakStatusVO);
  }

  @ApiOperation(value = "查询未中奖人员")
  @RequestMapping(value = "/noLottery", method = RequestMethod.GET)
  public RespMsg noLottery(HttpServletRequest request) {
    log.info("***********查询未中奖人员************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return personService.noLottery(companyId);
  }

  @ApiOperation(value = "删除签到人员")
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public RespMsg delete(@PathVariable Long id) {
    log.info("***********删除签到人员************");
    return personService.delete(id);
  }

  @ApiOperation(value = "手动录入签到人员")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public RespMsg add(@RequestBody PersonEntryVO personEntryVO, HttpServletRequest request) {
    log.info("***********手动录入签到人员************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    personEntryVO.setCompanyId(companyId);
    return personService.add(personEntryVO);
  }

}
