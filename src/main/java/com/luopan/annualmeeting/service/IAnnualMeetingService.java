package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.AnnualMeetingStateVO;

public interface IAnnualMeetingService {

  RespMsg setState(AnnualMeetingStateVO annualMeetingStateVO);

  RespMsg getState(Long companyId);

}
