package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Reward;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface RewardDao {

  int insert(Reward reward);

  int updateByPrimaryKeySelective(Reward reward);

  List<Reward> findAll(long companyId);

  Reward findById(long id);

  List<Reward> findNotLottery(long companyId);

}
