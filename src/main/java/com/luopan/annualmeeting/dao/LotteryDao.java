package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Lottery;
import com.luopan.annualmeeting.entity.vo.LotteryPersonRewardVO;
import com.luopan.annualmeeting.entity.vo.LotteryPersonVO;
import com.luopan.annualmeeting.entity.vo.LotteryRewardVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LotteryDao {

  int insert(Lottery lottery);

  List<Lottery> findAll();

  List<LotteryPersonRewardVO> findLotteryPeople();

  List<LotteryPersonVO> findLotteryPeopleByRewardId(long rewardId);

  List<LotteryRewardVO> findRewardByPersonId(long personId);

}
