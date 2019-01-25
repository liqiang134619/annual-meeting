package com.luopan.annualmeeting.util;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;

@SuppressWarnings("unchecked")
public final class ResultUtil {

  private ResultUtil() {

  }

  /**
   * 成功并带有返回值
   *
   * @param data 返回值
   */
  public static <T> RespMsg<T> success(T data) {
    RespMsg<T> respMsg = new RespMsg<>();
    respMsg.setErrCode(ErrCode.SUCCESS.getErrCode());
    respMsg.setErrMsg(ErrCode.SUCCESS.getErrMsg());
    respMsg.setData(data);
    return respMsg;
  }

  /**
   * 成功不带返回值
   */
  public static RespMsg success() {
    RespMsg respMsg = new RespMsg();
    respMsg.setErrCode(ErrCode.SUCCESS.getErrCode());
    respMsg.setErrMsg(ErrCode.SUCCESS.getErrMsg());
    respMsg.setData(Constant.EMPTY_STR);
    return respMsg;
  }

  /**
   * 失败
   */
  public static RespMsg error(ErrCode errCode) {
    RespMsg respMsg = new RespMsg();
    respMsg.setErrCode(errCode.getErrCode());
    respMsg.setErrMsg(errCode.getErrMsg());
    respMsg.setData(Constant.EMPTY_STR);
    return respMsg;
  }

  /**
   * 失败带返回值
   */
  public static <T> RespMsg<T> error(ErrCode errCode, T data) {
    RespMsg respMsg = new RespMsg();
    respMsg.setErrCode(errCode.getErrCode());
    respMsg.setErrMsg(errCode.getErrMsg());
    respMsg.setData(data);
    return respMsg;
  }

}
