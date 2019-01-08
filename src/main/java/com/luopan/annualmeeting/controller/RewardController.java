package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.RewardVO;
import com.luopan.annualmeeting.service.IRewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/reward")
public class RewardController {

  @Autowired
  private IRewardService rewardService;

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public RespMsg add(@RequestBody RewardVO rewardVO) {
    log.info("*************录入年会奖品***************");
    return rewardService.addReward(rewardVO);
  }

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public RespMsg list() {
    log.info("**************查看所有奖品**************");
    return rewardService.findAll();
  }

  @RequestMapping(value = "/notLottery", method = RequestMethod.GET)
  public RespMsg findNotLottery() {
    log.info("**************查看未抽奖品**************");
    return rewardService.findNotLottery();
  }

}
