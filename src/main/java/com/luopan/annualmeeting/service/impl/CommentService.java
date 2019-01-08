package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.CommentDao;
import com.luopan.annualmeeting.entity.Comment;
import com.luopan.annualmeeting.entity.vo.CommentPersonVO;
import com.luopan.annualmeeting.entity.vo.CommentVO;
import com.luopan.annualmeeting.service.ICommentService;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService implements ICommentService {

  @Autowired
  private CommentDao commentDao;

  @Transactional
  @Override
  public RespMsg add(CommentVO commentVO) {
    Comment comment = new Comment();
    BeanUtils.copyProperties(commentVO, comment);
    comment.fillDefaultProperty();
    commentDao.insert(comment);
    return ResultUtil.success();
  }

  @Override
  public RespMsg findByShowId(long showId) {
    List<CommentPersonVO> list = commentDao.findByShowId(showId);
    return ResultUtil.success(list);
  }
}
