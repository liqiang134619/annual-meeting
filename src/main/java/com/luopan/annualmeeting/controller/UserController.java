package com.luopan.annualmeeting.controller;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.UserVO;
import com.luopan.annualmeeting.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@Api(value = "后台用户管理", tags = "后台用户管理")
public class UserController {

  @Autowired
  private IUserService userService;

  @ApiOperation(value = "登录")
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public RespMsg login(@RequestBody UserVO userVO) {
    log.info("*************登录**************");
    return userService.login(userVO);
  }

}
