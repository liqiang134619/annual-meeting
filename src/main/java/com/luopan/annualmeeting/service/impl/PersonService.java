package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.OAuth2Param;
import com.luopan.annualmeeting.common.Constant.OAuth2Url;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.SignType;
import com.luopan.annualmeeting.common.Constant.Status;
import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.config.CommConfig;
import com.luopan.annualmeeting.dao.PersonDao;
import com.luopan.annualmeeting.entity.Person;
import com.luopan.annualmeeting.entity.vo.AccessTokenVO;
import com.luopan.annualmeeting.entity.vo.PersonIdVO;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonSpeakStatusVO;
import com.luopan.annualmeeting.entity.vo.PersonVO;
import com.luopan.annualmeeting.entity.vo.SignInPersonVO;
import com.luopan.annualmeeting.entity.vo.WeChatAuthVO;
import com.luopan.annualmeeting.entity.vo.WeChatCodeVO;
import com.luopan.annualmeeting.entity.vo.WeChatOpenidVO;
import com.luopan.annualmeeting.entity.vo.WeChatSignInVO;
import com.luopan.annualmeeting.entity.vo.WeChatUserInfoVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IPersonService;
import com.luopan.annualmeeting.util.HttpUtil;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class PersonService implements IPersonService {

  @Autowired
  private PersonDao personDao;

  @Autowired
  private CommConfig commConfig;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private ServerManageWebSocket serverManageWebSocket;

  @Override
  @Transactional
  public RespMsg signIn(PersonVO personVO) {
    Person person = new Person();
    if (personVO != null) {
      BeanUtils.copyProperties(personVO, person);
    }
    person.fillDefaultProperty();
    personDao.insert(person);
    PersonIdVO personIdVO = new PersonIdVO();
    personIdVO.setPersonId(person.getId());
    return ResultUtil.success(personIdVO);
  }

  @Override
  public RespMsg findSignInPeople() {
    List<SignInPersonVO> list = findSignInPersonList();
    return ResultUtil.success(list);
  }

  @Override
  public List<SignInPersonVO> findSignInPersonList() {
    List<SignInPersonVO> signInPeople = new LinkedList<>();
    List<Person> list = personDao.findAll();
    if (list != null && !list.isEmpty()) {
      list.forEach(person -> {
        SignInPersonVO vo = new SignInPersonVO();
        BeanUtils.copyProperties(person, vo);
        vo.setSignInTime(person.getCreateTime());
        signInPeople.add(vo);
      });
    }
    return signInPeople;
  }

  @Transactional
  @Override
  public RespMsg auth(WeChatSignInVO weChatSignInVO) {
    String openid = weChatSignInVO.getOpenid();
    if (StringUtils.isEmpty(openid)) {
      // 获取openid
      openid = getOpenidByCode(weChatSignInVO.getCode());
      if (StringUtils.isEmpty(openid)) {
        return ResultUtil.error(ErrCode.INVALID_CODE);
      }
    }

    // 判断是否已签到
    List<Person> personList = personDao.findByOpenid(openid);
    if (personList != null && !personList.isEmpty()) {
      return ResultUtil.error(ErrCode.HAD_SIGN_IN);
    }

    // 获取accessToken
    String accessTokenResult = HttpUtil.sendGet(OAuth2Url.ACCESS_TOKEN, String
        .format(OAuth2Param.ACCESS_TOKEN, commConfig.getWechatAppId(),
            commConfig.getWechatAppSecret()));
    AccessTokenVO accessTokenVO = JsonUtil.string2Obj(accessTokenResult, AccessTokenVO.class);
    // 获取userInfo
    String userInfoResult = HttpUtil.sendGet(OAuth2Url.USER_INFO, String
        .format(OAuth2Param.USER_INFO, accessTokenVO.getAccessToken(), openid));
    WeChatUserInfoVO weChatUserInfoVO = JsonUtil.string2Obj(userInfoResult, WeChatUserInfoVO.class);
    Person person = new Person();
    person.setAvatarUrl(weChatUserInfoVO.getHeadImgUrl());
    person.setCity(weChatUserInfoVO.getCity());
    person.setCountry(weChatUserInfoVO.getCountry());
    person.setProvince(weChatUserInfoVO.getProvince());
    person.setGender(weChatUserInfoVO.getSex());
    person.setName(weChatSignInVO.getName());
    person.setPhone(weChatSignInVO.getPhone());
    person.setNickname(weChatUserInfoVO.getNickname());
    person.setSignType(SignType.SCAN_CODE);
    person.setOpenid(openid);
    person.fillDefaultProperty();
    personDao.insert(person);

    PersonVO personVO = new PersonVO();
    BeanUtils.copyProperties(person, personVO);
    personVO.setSignTime(person.getCreateTime());

    // 推送签到消息
    SignInPersonVO signInPersonVO = new SignInPersonVO();
    BeanUtils.copyProperties(person, signInPersonVO);
    signInPersonVO.setSignInTime(person.getCreateTime());
    WebSocketMessageVO<SignInPersonVO> webSocketMessageVO = new WebSocketMessageVO<>();
    webSocketMessageVO.setType(WebSocketMessageType.SIGN_IN);
    webSocketMessageVO.setData(signInPersonVO);
    serverManageWebSocket.sendMessageAll(JsonUtil.obj2String(webSocketMessageVO));

    return ResultUtil.success(personVO);
  }

  @Override
  public RespMsg judgeSignIn(WeChatCodeVO weChatCodeVO) {
    String openid = getOpenidByCode(weChatCodeVO.getCode());
    if (StringUtils.isEmpty(openid)) {
      return ResultUtil.error(ErrCode.INVALID_CODE);
    }
    List<Person> personList = personDao.findByOpenid(openid);
    if (personList != null && !personList.isEmpty()) {
      Person person = personList.get(0);
      PersonVO personVO = new PersonVO();
      BeanUtils.copyProperties(person, personVO);
      personVO.setSignTime(person.getCreateTime());
      return ResultUtil.success(personVO);
    }
    WeChatOpenidVO weChatOpenidVO = new WeChatOpenidVO();
    weChatOpenidVO.setOpenid(openid);
    return ResultUtil.success(weChatOpenidVO);
  }

  // 用微信code换取openid
  private String getOpenidByCode(String code) {
    if (StringUtils.isEmpty(code)) {
      return null;
    }
    String result = HttpUtil.sendGet(OAuth2Url.OPENID, String
        .format(OAuth2Param.OPENID, commConfig.getWechatAppId(), commConfig.getWechatAppSecret(),
            code));
    WeChatAuthVO weChatAuthVO = JsonUtil.string2Obj(result, WeChatAuthVO.class);
    return weChatAuthVO.getOpenid();
  }

  @Override
  public RespMsg search(PersonSearchVO personSearchVO) {
    List<PersonVO> list = personDao.search(personSearchVO);
    return ResultUtil.success(list);
  }

  @Transactional
  @Override
  public RespMsg banned(PersonSpeakStatusVO personSpeakStatusVO) {
    Person person = new Person();
    BeanUtils.copyProperties(personSpeakStatusVO, person);
    person.setUpdateTime(new Date());
    personDao.updateSelective(person);
    if (Status.DISABLE == personSpeakStatusVO.getSpeakStatus()) {
      redisUtil.sSet(RedisKey.BANNED_PERSON_ID, personSpeakStatusVO.getId());
    } else {
      redisUtil.setRemove(RedisKey.BANNED_PERSON_ID, personSpeakStatusVO.getId());
    }
    return ResultUtil.success();
  }
}
