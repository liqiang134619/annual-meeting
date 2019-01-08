package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.ShowScore;
import com.luopan.annualmeeting.entity.vo.CommentPersonVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShowScoreDao {

  int insert(ShowScore showScore);

  List<ShowScore> findByPersonId(long personId);

  int count(@Param("personId") long personId, @Param("showId") long showId);

}
