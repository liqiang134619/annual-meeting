package com.luopan.annualmeeting.interceptor;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.util.Tools;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Created by lujw on 2019/1/16.
 */
@Slf4j
public class CompanyInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    log.info("拦截器 访问url：{}",request.getRequestURI());
    String companyId = request.getHeader(Constant.COMMON_PARAM_COMPANY_ID);
    if (StringUtils.isEmpty(companyId)) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return false;
    }
    Long id = Tools.getLong(companyId);
    if (id == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return false;
    }
    return true;
  }

}
