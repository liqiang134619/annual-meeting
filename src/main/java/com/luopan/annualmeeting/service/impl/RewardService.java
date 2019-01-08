package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.RewardDao;
import com.luopan.annualmeeting.entity.Reward;
import com.luopan.annualmeeting.entity.vo.RewardVO;
import com.luopan.annualmeeting.service.IRewardService;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.BeanUtils;
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
    Reward reward = new Reward();
    BeanUtils.copyProperties(rewardVO, reward);
    reward.fillDefaultProperty();
    rewardDao.insert(reward);
    return ResultUtil.success();
  }

  @Override
  public RespMsg findAll() {
    List<RewardVO> list = new LinkedList<>();
    List<Reward> rewards = rewardDao.findAll();
    if (rewards != null && !rewards.isEmpty()) {
      copyProperties(rewards, list);
    }
    return ResultUtil.success(list);
  }

  @Override
  public RespMsg findNotLottery() {
    List<RewardVO> list = new LinkedList<>();
    List<Reward> rewards = rewardDao.findNotLottery();
    if (rewards != null && !rewards.isEmpty()) {
      copyProperties(rewards, list);
    }
    return ResultUtil.success(list);
  }

  private void copyProperties(List<Reward> rewardList, List<RewardVO> voList) {
    rewardList.forEach(reward -> {
      RewardVO rewardVO = new RewardVO();
      BeanUtils.copyProperties(reward, rewardVO);
      voList.add(rewardVO);
    });
  }

}
