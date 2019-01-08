package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.ShowVote;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShowVoteDao {

  int insert(ShowVote showVote);

  List<ShowVote> findByPersonId(long personId);



}
