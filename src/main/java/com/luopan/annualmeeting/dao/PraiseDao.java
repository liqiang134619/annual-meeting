package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Praise;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PraiseDao {

  int insert(Praise praise);

  List<Praise> findByPersonId(long personId);

  int count(@Param("personId") long personId, @Param("showId") long showId);

}
