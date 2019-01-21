package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonSpeakStatusVO;
import com.luopan.annualmeeting.entity.vo.PersonVO;
import com.luopan.annualmeeting.entity.vo.SignInPersonVO;
import com.luopan.annualmeeting.entity.vo.WeChatCodeVO;
import com.luopan.annualmeeting.entity.vo.WeChatSignInVO;
import java.util.List;

public interface IPersonService {

  RespMsg signIn(PersonVO personVO);

  RespMsg findSignInPeople(Long companyId);

  List<SignInPersonVO> findSignInPersonList(Long companyId);

  RespMsg auth(WeChatSignInVO weChatSignInVO);

  RespMsg judgeSignIn(WeChatCodeVO weChatCodeVO);

  RespMsg search(PersonSearchVO personSearchVO);

  RespMsg banned(PersonSpeakStatusVO personSpeakStatusVO);

}
