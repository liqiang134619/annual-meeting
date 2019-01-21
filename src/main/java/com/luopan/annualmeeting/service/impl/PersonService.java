package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.OAuth2Url;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.SignType;
import com.luopan.annualmeeting.common.Constant.Status;
import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.PersonDao;
import com.luopan.annualmeeting.entity.Company;
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
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.HttpUtil;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
  public RespMsg findSignInPeople(Long companyId) {
    List<SignInPersonVO> list = findSignInPersonList(companyId);
    return ResultUtil.success(list);
  }

  @Override
  public List<SignInPersonVO> findSignInPersonList(Long companyId) {
    List<SignInPersonVO> signInPeople = new LinkedList<>();
    List<Person> list = personDao.findAll(companyId);
    if (!BeanUtil.isEmpty(list)) {
      signInPeople = list.stream().map(person -> {
        SignInPersonVO signInPersonVO = BeanUtil.copyProperties(person, SignInPersonVO.class);
        signInPersonVO.setSignInTime(person.getCreateTime());
        return signInPersonVO;
      }).collect(Collectors.toList());
    }
    return signInPeople;
  }

  @Transactional
  @Override
  public RespMsg auth(WeChatSignInVO weChatSignInVO) {
    Long companyId = weChatSignInVO.getCompanyId();
    Company company = redisUtil.hGet(RedisKey.COMPANY_MAP, companyId, Company.class);
    if (company == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    String openid = weChatSignInVO.getOpenid();
    if (StringUtils.isEmpty(openid)) {
      // 获取openid
      openid = getOpenidByCode(company.getWechatAppId(), company.getWechatAppSecret(),
          weChatSignInVO.getCode());
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
    String accessTokenResult = HttpUtil.get(String
        .format(OAuth2Url.ACCESS_TOKEN, company.getWechatAppId(), company.getWechatAppSecret()));
    AccessTokenVO accessTokenVO = JsonUtil.string2Obj(accessTokenResult, AccessTokenVO.class);
    if (accessTokenVO == null || StringUtils.isEmpty(accessTokenVO.getAccessToken())) {
      return ResultUtil.error(ErrCode.GET_USER_INFO_ERROR);
    }
    // 获取userInfo
    String userInfoResult = HttpUtil
        .get(String.format(OAuth2Url.USER_INFO, accessTokenVO.getAccessToken(), openid));
    WeChatUserInfoVO weChatUserInfoVO = JsonUtil.string2Obj(userInfoResult, WeChatUserInfoVO.class);
    if (weChatUserInfoVO == null) {
      return ResultUtil.error(ErrCode.GET_USER_INFO_ERROR);
    }
    if (weChatUserInfoVO.getSubscribe() == 0) {
      return ResultUtil.error(ErrCode.NO_ATTENTION_WECHAT);
    }
    if (StringUtils.isEmpty(weChatUserInfoVO.getNickname()) || StringUtils
        .isEmpty(weChatUserInfoVO.getHeadImgUrl())) {
      return ResultUtil.error(ErrCode.GET_USER_INFO_ERROR);
    }
    Person person = new Person();
    person.setAvatarUrl(weChatUserInfoVO.getHeadImgUrl()).setCity(weChatUserInfoVO.getCity())
        .setCountry(weChatUserInfoVO.getCountry())
        .setProvince(weChatUserInfoVO.getProvince())
        .setGender(weChatUserInfoVO.getSex())
        .setName(weChatSignInVO.getName())
        .setPhone(weChatSignInVO.getPhone())
        .setNickname(weChatUserInfoVO.getNickname())
        .setSignType(SignType.SCAN_CODE)
        .setOpenid(openid)
        .setCompanyId(weChatSignInVO.getCompanyId())
        .fillDefaultProperty();
    personDao.insert(person);

    PersonVO personVO = BeanUtil.copyProperties(person, PersonVO.class)
        .setSignTime(person.getCreateTime()).setCompanyName(company.getName());

    // 推送签到消息
    CompletableFuture.runAsync(() -> {
      SignInPersonVO signInPersonVO = new SignInPersonVO();
      BeanUtils.copyProperties(person, signInPersonVO);
      signInPersonVO.setSignInTime(person.getCreateTime());
      WebSocketMessageVO<List<SignInPersonVO>> webSocketMessageVO = new WebSocketMessageVO<>(
          WebSocketMessageType.SIGN_IN, Arrays.asList(signInPersonVO));
      serverManageWebSocket.sendMessageAll(JsonUtil.obj2String(webSocketMessageVO), companyId);
    });

    return ResultUtil.success(personVO);
  }

  @Override
  public RespMsg judgeSignIn(WeChatCodeVO weChatCodeVO) {
    Long companyId = weChatCodeVO.getCompanyId();
    Company company = redisUtil.hGet(RedisKey.COMPANY_MAP, companyId, Company.class);
    if (company == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    String openid = getOpenidByCode(company.getWechatAppId(), company.getWechatAppSecret(),
        weChatCodeVO.getCode());
    if (StringUtils.isEmpty(openid)) {
      return ResultUtil.error(ErrCode.INVALID_CODE);
    }

    List<Person> personList = personDao.findByOpenid(openid);
    if (personList != null && !personList.isEmpty()) {
      Optional<Person> optionalPerson = personList.stream()
          .filter(p -> companyId.equals(p.getCompanyId())).findFirst();
      if (optionalPerson.isPresent()) {
        Person person = optionalPerson.get();
        PersonVO personVO = BeanUtil.copyProperties(person, PersonVO.class)
            .setSignTime(person.getCreateTime()).setCompanyName(company.getName());
        return ResultUtil.success(personVO);
      }
    }
    WeChatOpenidVO weChatOpenidVO = new WeChatOpenidVO();
    weChatOpenidVO.setOpenid(openid);
    return ResultUtil.success(weChatOpenidVO);
  }

  // 用微信code换取openid
  private String getOpenidByCode(String wechatAppId, String wechatAppSecret, String code) {
    if (StringUtils.isEmpty(code) || StringUtils.isEmpty(wechatAppId) || StringUtils
        .isEmpty(wechatAppSecret)) {
      return null;
    }
    String result = HttpUtil.get(
        String
            .format(OAuth2Url.OPENID, wechatAppId, wechatAppSecret, code));
    WeChatAuthVO weChatAuthVO = Optional.ofNullable(JsonUtil.string2Obj(result, WeChatAuthVO.class))
        .orElse(new WeChatAuthVO());
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
    Person person = BeanUtil.copyProperties(personSpeakStatusVO, Person.class);
    person.setUpdateTime(new Date());
    personDao.updateSelective(person);
    if (Status.DISABLE == personSpeakStatusVO.getSpeakStatus()) {
      redisUtil.sSet(RedisKey.BANNED_PERSON_ID, personSpeakStatusVO.getId());
    } else {
      redisUtil.sRemove(RedisKey.BANNED_PERSON_ID, personSpeakStatusVO.getId());
    }
    return ResultUtil.success();
  }
}
