package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.AnnualMeetingStateVO;
import com.luopan.annualmeeting.service.IAnnualMeetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/annualMeeting")
public class AnnualMeetingController {

  @Autowired
  private IAnnualMeetingService annualMeetingService;

  @RequestMapping(value = "/setState", method = RequestMethod.POST)
  public RespMsg setState(@RequestBody AnnualMeetingStateVO annualMeetingStateVO) {
    log.info("**********设置年会状态**********");
    return annualMeetingService.setState(annualMeetingStateVO);
  }

}
