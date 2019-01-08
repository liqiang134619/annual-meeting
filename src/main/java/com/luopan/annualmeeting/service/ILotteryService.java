package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;

public interface ILotteryService {

  RespMsg lottery(long rewardId);

  RespMsg findAllLotteryPeople();

  RespMsg findRewardLotteryPeople(long rewardId);

  RespMsg findRewardByPersonId(long personId);

}
