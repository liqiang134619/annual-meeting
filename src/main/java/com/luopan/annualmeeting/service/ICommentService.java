package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.CommentVO;

public interface ICommentService {

  RespMsg add(CommentVO commentVO);

  RespMsg findByShowId(long showId);

}
