package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.ShowVoteStateVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteVO;

public interface IShowVoteService {

  RespMsg vote(ShowVoteVO showVoteVO);

  RespMsg my(Long personId);

  RespMsg remain(Long personId, Long companyId);

}
