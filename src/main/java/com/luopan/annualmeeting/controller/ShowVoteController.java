package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.ShowVoteStateVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteVO;
import com.luopan.annualmeeting.service.IShowVoteService;
import com.luopan.annualmeeting.util.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@RequestMapping("/vote")
@Api(value = "投票", tags = "投票")
public class ShowVoteController {

  @Autowired
  private IShowVoteService showVoteService;

  @ApiOperation(value = "投票")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public RespMsg add(@RequestBody ShowVoteVO showVoteVO, HttpServletRequest request) {
    log.info("***********投票*************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    showVoteVO.setCompanyId(companyId);
    return showVoteService.vote(showVoteVO);
  }

  @ApiOperation(value = "获取我的投票")
  @RequestMapping(value = "/my/{personId}", method = RequestMethod.GET)
  public RespMsg my(@PathVariable Long personId) {
    log.info("***********获取我的投票*************");
    return showVoteService.my(personId);
  }

  @ApiOperation(value = "获取剩余投票次数")
  @RequestMapping(value = "/remain/{personId}", method = RequestMethod.GET)
  public RespMsg remain(@PathVariable Long personId, HttpServletRequest request) {
    log.info("***********获取剩余投票次数*************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return showVoteService.remain(personId, companyId);
  }

}
