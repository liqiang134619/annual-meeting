package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Comment;
import com.luopan.annualmeeting.entity.Show;
import com.luopan.annualmeeting.entity.vo.CommentPersonVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommentDao {

  int insert(Comment comment);

  List<CommentPersonVO> findByShowId(long showId);

}
