package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.RewardLotteryVO;

public interface ILotteryService {

  RespMsg lottery(long rewardId);

  RespMsg findAllLotteryPeople();

  RespMsg findRewardLotteryPeople(long rewardId);

  RespMsg findRewardByPersonId(long personId);

  RespMsg save(RewardLotteryVO rewardLotteryVO);

}
