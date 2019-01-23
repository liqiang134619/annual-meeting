package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.RewardLotteryVO;

public interface ILotteryService {

  RespMsg lottery(Long rewardId);

  RespMsg findAllLotteryPeople(Long companyId);

  RespMsg findRewardLotteryPeople(Long rewardId);

  RespMsg findRewardByPersonId(Long personId);

  RespMsg save(RewardLotteryVO rewardLotteryVO);

  RespMsg empty(Long companyId);

}
