package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.RewardVO;

public interface IRewardService {

  RespMsg addReward(RewardVO rewardVO);

  RespMsg findAll(Long companyId);

  RespMsg findNotLottery(Long companyId);

  RespMsg delete(Long id);

}
