package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.service.ICountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/count")
public class CountController {

  @Autowired
  private ICountService countService;

  @RequestMapping(value = "/total", method = RequestMethod.GET)
  public RespMsg total() {
    log.info("*************统计***************");
    return countService.total();
  }

}
