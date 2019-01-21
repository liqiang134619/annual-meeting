package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.RewardDao;
import com.luopan.annualmeeting.entity.Reward;
import com.luopan.annualmeeting.entity.vo.RewardVO;
import com.luopan.annualmeeting.service.IRewardService;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RewardService implements IRewardService {

  @Autowired
  private RewardDao rewardDao;

  @Transactional
  @Override
  public RespMsg addReward(RewardVO rewardVO) {
    Reward reward = BeanUtil.copyProperties(rewardVO, Reward.class);
    reward.fillDefaultProperty();
    rewardDao.insert(reward);
    return ResultUtil.success();
  }

  @Override
  public RespMsg findAll(Long companyId) {
    List<RewardVO> list = new LinkedList<>();
    List<Reward> rewards = rewardDao.findAll(companyId);
    if (rewards != null && !rewards.isEmpty()) {
      list = BeanUtil.copyProperties(rewards, RewardVO.class);
    }
    return ResultUtil.success(list);
  }

  @Override
  public RespMsg findNotLottery(Long companyId) {
    List<RewardVO> list = new LinkedList<>();
    List<Reward> rewards = rewardDao.findNotLottery(companyId);
    if (rewards != null && !rewards.isEmpty()) {
      list = BeanUtil.copyProperties(rewards, RewardVO.class);
    }
    return ResultUtil.success(list);
  }

}
