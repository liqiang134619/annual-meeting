package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PersonIdVO;
import com.luopan.annualmeeting.entity.vo.PraiseVO;
import com.luopan.annualmeeting.entity.vo.ShowScoreVO;
import com.luopan.annualmeeting.entity.vo.ShowVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteNowVO;
import com.luopan.annualmeeting.service.IShowService;
import com.luopan.annualmeeting.util.Tools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/show")
@Api(value = "节目", tags = "节目")
public class ShowController {

  @Autowired
  private IShowService showService;

  @ApiOperation(value = "查看节目单")
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public RespMsg findAll(HttpServletRequest request) {
    log.info("*************查看节目单*************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return showService.findAll(companyId);
  }

  @ApiOperation(value = "录入节目")
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public RespMsg add(@RequestBody ShowVO showVO, HttpServletRequest request) {
    log.info("**************录入节目**************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    showVO.setCompanyId(companyId);
    return showService.insert(showVO);
  }

  @ApiOperation(value = "设置节目可投票")
  @RequestMapping(value = "/voteNow", method = RequestMethod.POST)
  public RespMsg voteNow(@RequestBody ShowVoteNowVO showVoteNowVO, HttpServletRequest request) {
    log.info("**************设置节目可投票**************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    showVoteNowVO.setCompanyId(companyId);
    return showService.voteNow(showVoteNowVO);
  }

  @ApiOperation(value = "节目点赞")
  @RequestMapping(value = "/praise", method = RequestMethod.POST)
  public RespMsg praise(@RequestBody PraiseVO praiseVO) {
    log.info("**************节目点赞**************");
    return showService.praise(praiseVO);
  }

  @ApiOperation(value = "节目评分")
  @RequestMapping(value = "/score", method = RequestMethod.POST)
  public RespMsg score(@RequestBody ShowScoreVO showScoreVO) {
    log.info("**************节目评分**************");
    return showService.score(showScoreVO);
  }

  @ApiOperation(value = "查看节目评分")
  @RequestMapping(value = "/scoreList", method = RequestMethod.POST)
  public RespMsg scoreList(@RequestBody PersonIdVO personIdVO) {
    log.info("**************查看节目评分**************");
    return showService.findShowScorePraiseCommentVOList(personIdVO.getPersonId());
  }

  @ApiOperation(value = "删除节目")
  @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
  public RespMsg delete(@PathVariable Long id) {
    log.info("**************删除节目**************");
    return showService.delete(id);
  }

  @ApiOperation(value = "查看节目投票")
  @RequestMapping(value = "/vote", method = RequestMethod.GET)
  public RespMsg vote(HttpServletRequest request) {
    log.info("**************查看节目投票**************");
    Long companyId = Tools.getLongFromRequest(request, Constant.COMMON_PARAM_COMPANY_ID);
    return showService.vote(companyId);
  }

}
