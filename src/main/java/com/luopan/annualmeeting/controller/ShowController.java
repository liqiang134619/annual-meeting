package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PersonIdVO;
import com.luopan.annualmeeting.entity.vo.PraiseVO;
import com.luopan.annualmeeting.entity.vo.ShowScoreVO;
import com.luopan.annualmeeting.entity.vo.ShowVO;
import com.luopan.annualmeeting.service.IShowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/show")
public class ShowController {

  @Autowired
  private IShowService showService;

  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public RespMsg findAll() {
    log.info("*************查看节目单*************");
    return showService.findAll();
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public RespMsg add(@RequestBody ShowVO showVO) {
    log.info("**************录入节目**************");
    return showService.insert(showVO);
  }

  @RequestMapping(value = "/praise", method = RequestMethod.POST)
  public RespMsg praise(@RequestBody PraiseVO praiseVO) {
    log.info("**************节目点赞**************");
    return showService.praise(praiseVO);
  }

  @RequestMapping(value = "/score", method = RequestMethod.POST)
  public RespMsg score(@RequestBody ShowScoreVO showScoreVO) {
    log.info("**************节目评分**************");
    return showService.score(showScoreVO);
  }

  @RequestMapping(value = "/scoreList", method = RequestMethod.POST)
  public RespMsg scoreList(@RequestBody PersonIdVO personIdVO) {
    log.info("**************查看节目评分**************");
    return showService.findShowScorePraiseCommentVOList(personIdVO.getPersonId());
  }

}
