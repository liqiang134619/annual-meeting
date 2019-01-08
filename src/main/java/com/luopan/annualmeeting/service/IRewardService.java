package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.RewardVO;

public interface IRewardService {

  RespMsg addReward(RewardVO rewardVO);

  RespMsg findAll();

  RespMsg findNotLottery();

}
