package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.PersonEntryVO;
import com.luopan.annualmeeting.entity.vo.PersonFaceSignInVO;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonBindingVO;
import com.luopan.annualmeeting.entity.vo.PersonSpeakStatusVO;
import com.luopan.annualmeeting.entity.vo.SignInPersonVO;
import com.luopan.annualmeeting.entity.vo.WeChatCodeVO;
import java.util.List;

public interface IPersonService {

  RespMsg faceSignIn(PersonFaceSignInVO personFaceSignInVO);

  RespMsg findSignInPeople(Long companyId);

  List<SignInPersonVO> findSignInPersonList(Long companyId);

  RespMsg judgeSignIn(WeChatCodeVO weChatCodeVO);

  RespMsg binding(PersonBindingVO personBindingVO);

  RespMsg search(PersonSearchVO personSearchVO);

  RespMsg banned(PersonSpeakStatusVO personSpeakStatusVO);

  RespMsg noLottery(Long companyId);

  RespMsg delete(Long id);

  RespMsg add(PersonEntryVO personEntryVO);

}
