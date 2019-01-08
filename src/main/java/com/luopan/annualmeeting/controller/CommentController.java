package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.CommentVO;
import com.luopan.annualmeeting.service.ICommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/comment")
@Api(value = "评论", tags = "评论")
public class CommentController {

  @Autowired
  private ICommentService commentService;

  @ApiOperation(value = "查看节目评论")
  @ApiImplicitParam(name = "id", value = "节目Id", required = true, dataType = "Long", paramType = "path")
  @RequestMapping(value = "/show/{id}", method = RequestMethod.GET, produces = "application/json")
  public RespMsg findByShowId(@PathVariable("id") Long showId) {
    log.info("*************查看节目评论*************");
    return commentService.findByShowId(showId);
  }

  @ApiOperation(value = "评论")
  @ApiImplicitParam(name = "commentVO", value = "评论实体commentVO", required = true, dataType = "CommentVO")
  @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
  public RespMsg add(@RequestBody CommentVO commentVO) {
    log.info("**************评论节目**************");
    return commentService.add(commentVO);
  }

}
