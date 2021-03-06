package com.luopan.annualmeeting.interceptor;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.util.RedisUtil;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 年会开始拦截器
 */
public class AnnualMeetingStartInterceptor implements HandlerInterceptor {

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    response.setContentType(Constant.DEFAULT_CONTENT_TYPE);
    PrintWriter printWriter = response.getWriter();
    Date now = new Date();
    // 开始时间
    /*Date startTime = (Date) redisUtil.get(RedisKey.ANNUAL_MEETING_START_TIME);
    if (startTime == null || startTime.compareTo(now) > 0) {
      RespMsg respMsg = ResultUtil.error(ErrCode.ANNUAL_MEETING_NOT_STARTED);
      printWriter.write(JsonUtil.obj2String(respMsg));
      return false;
    }*/
    return true;
  }
}
