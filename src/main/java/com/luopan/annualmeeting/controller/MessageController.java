package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.MessageCheckVO;
import com.luopan.annualmeeting.entity.vo.MessageGetVO;
import com.luopan.annualmeeting.entity.vo.MessageSearchVO;
import com.luopan.annualmeeting.entity.vo.MessageSendVO;
import com.luopan.annualmeeting.entity.vo.MessageTopVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.util.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/message")
@Api(value = "留言", tags = "留言")
public class MessageController {

  @Autowired
  private IMessageService messageService;

  @ApiOperation(value = "搜索留言")
  @RequestMapping(value = "/search", method = RequestMethod.POST)
  public RespMsg search(@RequestBody MessageSearchVO messageSearchVO, HttpServletRequest request) {
    log.info("**********搜索留言**********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    messageSearchVO.setCompanyId(companyId);
    return messageService.search(messageSearchVO);
  }

  @ApiOperation(value = "审批留言")
  @RequestMapping(value = "/check", method = RequestMethod.POST)
  public RespMsg check(@RequestBody MessageCheckVO messageCheckVO) {
    log.info("**********审批留言**********");
    return messageService.check(messageCheckVO);
  }

  @ApiOperation(value = "置顶留言")
  @RequestMapping(value = "/top", method = RequestMethod.POST)
  public RespMsg top(@RequestBody MessageTopVO messageTopVO) {
    log.info("**********置顶留言**********");
    return messageService.top(messageTopVO);
  }

  @ApiOperation(value = "刷新加载留言")
  @RequestMapping(value = "/more", method = RequestMethod.POST)
  public RespMsg getMoreMessages(@RequestBody MessageGetVO messageGetVO,
      HttpServletRequest request) {
    log.info("**********刷新加载留言**********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    messageGetVO.setCompanyId(companyId);
    return messageService.getMoreMessages(messageGetVO);
  }

  @ApiOperation(value = "发送留言")
  @RequestMapping(value = "/send", method = RequestMethod.POST)
  public RespMsg send(@RequestBody MessageSendVO messageSendVO, HttpServletRequest request) {
    log.info("**********发送留言**********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    messageSendVO.setCompanyId(companyId);
    return messageService.sendMessage(messageSendVO);
  }

  @ApiOperation(value = "暂停")
  @RequestMapping(value = "/pause", method = RequestMethod.GET)
  public RespMsg pause(HttpServletRequest request) {
    log.info("**********暂停**********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return messageService.pause(companyId);
  }

  @ApiOperation(value = "恢复")
  @RequestMapping(value = "/resume", method = RequestMethod.GET)
  public RespMsg resume(HttpServletRequest request) {
    log.info("**********恢复**********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return messageService.resume(companyId);
  }

  @ApiOperation(value = "换一批")
  @RequestMapping(value = "/change", method = RequestMethod.GET)
  public RespMsg change(HttpServletRequest request) {
    log.info("**********换一批**********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return messageService.change(companyId);
  }

}
