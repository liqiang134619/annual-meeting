package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.AnnualMeetingStateVO;
import com.luopan.annualmeeting.service.IAnnualMeetingService;
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
@RequestMapping("/annualMeeting")
@Api(value = "年会配置", tags = "年会配置")
public class AnnualMeetingController {

  @Autowired
  private IAnnualMeetingService annualMeetingService;

  @ApiOperation(value = "修改年会配置")
  @RequestMapping(value = "/setState", method = RequestMethod.POST)
  public RespMsg setState(@RequestBody AnnualMeetingStateVO annualMeetingStateVO,
      HttpServletRequest request) {
    log.info("**********设置年会状态**********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    annualMeetingStateVO.setCompanyId(companyId);
    return annualMeetingService.setState(annualMeetingStateVO);
  }

  @ApiOperation(value = "获取年会配置")
  @RequestMapping(value = "/state", method = RequestMethod.GET)
  public RespMsg getState(HttpServletRequest request) {
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    log.info("**********获取年会状态**********");
    return annualMeetingService.getState(companyId);
  }

}
