package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.RewardLotteryVO;
import com.luopan.annualmeeting.service.ILotteryService;
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
@RequestMapping("/lottery")
@Api(tags = "抽奖", value = "抽奖")
public class LotteryController {

  @Autowired
  private ILotteryService lotteryService;

  @ApiOperation(value = "抽奖")
  @RequestMapping(value = "/reward/{id}", method = RequestMethod.GET)
  public RespMsg lottery(@PathVariable Long id) {
    log.info("************抽奖************");
    return lotteryService.lottery(id);
  }

  @ApiOperation(value = "查看所有中奖人员")
  @RequestMapping(value = "/findAllLotteryPeople", method = RequestMethod.GET)
  public RespMsg findAllLotteryPeople() {
    log.info("**********查看所有中奖人员**********");
    return lotteryService.findAllLotteryPeople();
  }

  @ApiOperation(value = "查看单个奖励中奖人员")
  @RequestMapping(value = "/findLotteryPeople/{rewardId}", method = RequestMethod.GET)
  public RespMsg findRewardLotteryPeople(@PathVariable Long rewardId) {
    log.info("**********查看单个奖励中奖人员**********");
    return lotteryService.findRewardLotteryPeople(rewardId);
  }

  @ApiOperation(value = "查看个人中奖纪录")
  @RequestMapping(value = "/findPersonReward/{personId}", method = RequestMethod.GET)
  public RespMsg findPersonReward(@PathVariable Long personId) {
    log.info("**********查看人员中奖纪录**********");
    return lotteryService.findRewardByPersonId(personId);
  }

  @ApiOperation(value = "保存中奖结果")
  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public RespMsg save(@RequestBody RewardLotteryVO rewardLotteryVO, HttpServletRequest request) {
    log.info("**********保存中奖结果***********");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    rewardLotteryVO.setCompanyId(companyId);
    return lotteryService.save(rewardLotteryVO);
  }

}
