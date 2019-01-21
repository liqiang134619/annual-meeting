package com.luopan.annualmeeting.interceptor;

import com.luopan.annualmeeting.util.RedisUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 年会结束拦截器
 */
public class AnnualMeetingEndInterceptor implements HandlerInterceptor {

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    /*response.setContentType(Constant.DEFAULT_CONTENT_TYPE);
    PrintWriter printWriter = response.getWriter();
    Date now = new Date();
    // 结束时间
    Date endTime = (Date) redisUtil.get(RedisKey.ANNUAL_MEETING_END_TIME);
    if (endTime != null && endTime.compareTo(now) <= 0) {
      RespMsg respMsg = ResultUtil.error(ErrCode.ANNUAL_MEETING_ENDED);
      printWriter.write(JsonUtil.obj2String(respMsg));
      return false;
    }*/
    return true;
  }
}
