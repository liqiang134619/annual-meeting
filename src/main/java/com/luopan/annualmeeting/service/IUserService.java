package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.UserVO;

public interface IUserService {

  RespMsg login(UserVO userVO);

}
