package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.service.ILotteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/lottery")
public class LotteryController {

  @Autowired
  private ILotteryService lotteryService;

  @RequestMapping(value = "/reward/{id}", method = RequestMethod.GET)
  public RespMsg lottery(@PathVariable Long id) {
    log.info("************抽奖************");
    return lotteryService.lottery(id);
  }

  @RequestMapping(value = "/findAllLotteryPeople", method = RequestMethod.GET)
  public RespMsg findAllLotteryPeople() {
    log.info("**********查看所有中奖人员**********");
    return lotteryService.findAllLotteryPeople();
  }

  @RequestMapping(value = "/findLotteryPeople/{rewardId}", method = RequestMethod.GET)
  public RespMsg findRewardLotteryPeople(@PathVariable Long rewardId) {
    log.info("**********查看单个奖励中奖人员**********");
    return lotteryService.findRewardLotteryPeople(rewardId);
  }

  @RequestMapping(value = "/findPersonReward/{personId}", method = RequestMethod.GET)
  public RespMsg findPersonReward(@PathVariable Long personId) {
    log.info("**********查看人员中奖纪录**********");
    return lotteryService.findRewardByPersonId(personId);
  }

}
