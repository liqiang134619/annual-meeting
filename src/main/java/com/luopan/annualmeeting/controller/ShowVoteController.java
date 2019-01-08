package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.ShowVoteStateVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteVO;
import com.luopan.annualmeeting.service.IShowVoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/vote")
public class ShowVoteController {

  @Autowired
  private IShowVoteService showVoteService;

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public RespMsg add(@RequestBody ShowVoteVO showVoteVO) {
    log.info("***********投票*************");
    return showVoteService.vote(showVoteVO);
  }

  @RequestMapping(value = "/setState", method = RequestMethod.POST)
  public RespMsg setState(@RequestBody ShowVoteStateVO showVoteStateVO) {
    log.info("***********设置投票*************");
    return showVoteService.setState(showVoteStateVO);
  }

}
