package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Show;
import com.luopan.annualmeeting.entity.vo.ShowScorePraiseCommentVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteCountVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShowDao {

  List<Show> findAll();

  int insert(Show show);

  List<ShowScorePraiseCommentVO> findShowScorePraiseCommentVOList();

  List<ShowVoteCountVO> findShowVoteCountVOList();

}
