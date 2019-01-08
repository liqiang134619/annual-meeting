package com.luopan.annualmeeting.handler;

import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public RespMsg exception(Exception e) {
    log.error("服务器错误", e);
    RespMsg<String> error = ResultUtil.error(ErrCode.SERVER_ERROR);
    return error;
  }

}
