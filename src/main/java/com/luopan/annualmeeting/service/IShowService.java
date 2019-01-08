package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PraiseVO;
import com.luopan.annualmeeting.entity.vo.ShowScoreVO;
import com.luopan.annualmeeting.entity.vo.ShowVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteCountVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteVO;
import java.util.List;

public interface IShowService {

  RespMsg findAll();

  RespMsg insert(ShowVO showVO);

  RespMsg praise(PraiseVO praiseVO);

  RespMsg score(ShowScoreVO showScoreVO);

  RespMsg findShowScorePraiseCommentVOList(long personId);

  List<ShowVoteCountVO> findShowVoteCountVOList();

}
