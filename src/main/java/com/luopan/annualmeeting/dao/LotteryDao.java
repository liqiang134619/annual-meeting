package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Lottery;
import com.luopan.annualmeeting.entity.vo.LotteryPersonRewardVO;
import com.luopan.annualmeeting.entity.vo.LotteryPersonVO;
import com.luopan.annualmeeting.entity.vo.LotteryRewardVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LotteryDao {

  int insert(Lottery lottery);

  List<Lottery> findAll(long companyId);

  List<LotteryPersonRewardVO> findLotteryPeople(long companyId);

  List<LotteryPersonVO> findLotteryPeopleByRewardId(long rewardId);

  List<LotteryRewardVO> findRewardByPersonId(long personId);

  int insertList(List<Lottery> list);

  int empty(long companyId);

}
