package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.RewardVO;
import com.luopan.annualmeeting.service.IRewardService;
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
@RequestMapping("/reward")
@Api(value = "奖品", tags = "奖品")
public class RewardController {

  @Autowired
  private IRewardService rewardService;

  @ApiOperation(value = "录入奖品")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public RespMsg add(@RequestBody RewardVO rewardVO, HttpServletRequest request) {
    log.info("*************录入年会奖品***************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    rewardVO.setCompanyId(companyId);
    return rewardService.addReward(rewardVO);
  }

  @ApiOperation(value = "删除奖品")
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public RespMsg delete(@PathVariable Long id) {
    log.info("*************删除年会奖品***************");
    return rewardService.delete(id);
  }

  @ApiOperation(value = "查看所有奖品")
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public RespMsg list(HttpServletRequest request) {
    log.info("**************查看所有奖品**************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return rewardService.findAll(companyId);
  }

  @ApiOperation(value = "查看未抽奖品")
  @RequestMapping(value = "/notLottery", method = RequestMethod.GET)
  public RespMsg findNotLottery(HttpServletRequest request) {
    log.info("**************查看未抽奖品**************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return rewardService.findNotLottery(companyId);
  }

}
