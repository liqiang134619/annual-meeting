package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant;
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
import com.luopan.annualmeeting.entity.vo.PersonEntryVO;
import com.luopan.annualmeeting.entity.vo.PersonExampleVO;
import com.luopan.annualmeeting.entity.vo.PersonFaceSignInVO;
import com.luopan.annualmeeting.entity.vo.PersonIdVO;
import com.luopan.annualmeeting.entity.vo.PersonNoLotteryVO;
import com.luopan.annualmeeting.entity.vo.PersonSearchVO;
import com.luopan.annualmeeting.entity.vo.PersonSpeakStatusVO;
import com.luopan.annualmeeting.entity.vo.PersonVO;
import com.luopan.annualmeeting.entity.vo.SignInPersonVO;
import com.luopan.annualmeeting.entity.vo.WeChatAuthVO;
import com.luopan.annualmeeting.entity.vo.WeChatCodeVO;
import com.luopan.annualmeeting.entity.vo.WeChatUserInfoVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IPersonService;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.HttpUtil;
import com.luopan.annualmeeting.util.IdcardUtil;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
  public RespMsg faceSignIn(PersonFaceSignInVO personFaceSignInVO) {
    String name = personFaceSignInVO.getName();
    String cardNumber = personFaceSignInVO.getCardNumber();
    Date enterTime = personFaceSignInVO.getEnterTime();
    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(cardNumber) || enterTime == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    // 验证身份证合法性
    if (!IdcardUtil.isValidatedAllIdcard(cardNumber)) {
      return ResultUtil.error(ErrCode.PERSON_CARD_NUMBER_ERROR);
    }

    // 判断是否已签到
    long count = personDao
        .countByExample(
            new PersonExampleVO().setCompanyId(Constant.COMPANY_ID_LP).setCardNumber(cardNumber));
    if (count != 0) {
      return ResultUtil.error(ErrCode.HAD_SIGN_IN);
    }

    // 从身份证中获取性别并设置头像
    String avatarUrl = Constant.AVATAR_MEN;
    Integer gender = IdcardUtil.getGenderFromIdcard(cardNumber);
    if (gender != null && gender == Constant.GENDER_WOMEN) {
      avatarUrl = Constant.AVATAR_WOMEN;
    }

    Person person = BeanUtil.copyProperties(personFaceSignInVO, Person.class);
    person.setNickname(name).setSpeakStatus(Status.ENABLE).setGender(gender).setAvatarUrl(avatarUrl)
        .setSignType(SignType.FACE_RECOGNITION).setCompanyId(Constant.COMPANY_ID_LP)
        .setCreateTime(enterTime).setUpdateTime(enterTime).setStatus(Status.ENABLE);

    int rows = personDao.insert(person);
    if (rows == 0) {
      return ResultUtil.error(ErrCode.PERSON_FACE_SIGN_IN_ERROR);
    }

    PersonIdVO personIdVO = new PersonIdVO().setPersonId(person.getId());
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
  public RespMsg judgeSignIn(WeChatCodeVO weChatCodeVO) {
    Date now = new Date();

    // 查询缓存中的企业
    Long companyId = weChatCodeVO.getCompanyId();
    Company company = redisUtil.hGet(RedisKey.COMPANY_MAP, companyId, Company.class);
    if (company == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    // 微信网页授权
    String result = HttpUtil.get(String
        .format(OAuth2Url.AUTH, company.getWechatAppId(), company.getWechatAppSecret(),
            weChatCodeVO.getCode()));
    WeChatAuthVO weChatAuthVO = JsonUtil.string2Obj(result, WeChatAuthVO.class);
    if (weChatAuthVO == null || StringUtils.isEmpty(weChatAuthVO.getOpenid()) || StringUtils
        .isEmpty(weChatAuthVO.getAccessToken())) {
      return ResultUtil.error(ErrCode.INVALID_CODE);
    }

    // 判断是否已签到
    List<Person> personList = personDao.findByExample(
        new PersonExampleVO().setCompanyId(companyId).setOpenid(weChatAuthVO.getOpenid()));
    if (BeanUtil.isNotEmpty(personList)) {
      Person person = personList.get(0);
      SignInPersonVO signInPersonVO = BeanUtil.copyProperties(person, SignInPersonVO.class)
          .setSignInTime(person.getCreateTime()).setCompanyName(company.getName());
      return ResultUtil.success(signInPersonVO);
    }

    // 未签到或者人脸识别签到初次扫描二维码
    // 获取userInfo
    String userInfoResult = HttpUtil
        .get(String
            .format(OAuth2Url.USER_INFO, weChatAuthVO.getAccessToken(), weChatAuthVO.getOpenid()));
    WeChatUserInfoVO weChatUserInfoVO = JsonUtil.string2Obj(userInfoResult, WeChatUserInfoVO.class);
    if (weChatUserInfoVO == null || StringUtils.isEmpty(weChatUserInfoVO.getNickname())
        || StringUtils.isEmpty(weChatUserInfoVO.getHeadimgurl())) {
      return ResultUtil.error(ErrCode.GET_USER_INFO_ERROR);
    }

    // 如果有身份证代表人脸识别签到
    if (!StringUtils.isEmpty(weChatCodeVO.getCardNumber())) {
      personList = personDao.findByExample(new PersonExampleVO().setCompanyId(companyId)
          .setCardNumber(weChatCodeVO.getCardNumber()));
      if (BeanUtil.isEmpty(personList)) {
        return ResultUtil.error(ErrCode.PERSON_FACE_SIGN_IN_NOT_FOUND);
      }

      Person person = personList.get(0);
      if (!StringUtils.isEmpty(person.getOpenid())) {
        return ResultUtil.error(ErrCode.PERSON_QR_CODE_HAD_BINDING);
      }

      // 更新用户信息
      person.setAvatarUrl(weChatUserInfoVO.getHeadimgurl())
          .setNickname(weChatUserInfoVO.getNickname()).setOpenid(weChatUserInfoVO.getOpenid())
          .setCountry(weChatUserInfoVO.getCountry()).setCity(weChatUserInfoVO.getCity())
          .setProvince(weChatUserInfoVO.getProvince()).setUpdateTime(now);
      personDao.updateSelective(person);

      SignInPersonVO signInPersonVO = BeanUtil.copyProperties(person, SignInPersonVO.class)
          .setSignInTime(person.getCreateTime()).setCompanyName(company.getName());

      // 推送签到消息
      sendSignInSuccessMessage(signInPersonVO, companyId);

      return ResultUtil.success(signInPersonVO);
    }

    // 未签到
    Person person = new Person();
    person.setAvatarUrl(weChatUserInfoVO.getHeadimgurl()).setCity(weChatUserInfoVO.getCity())
        .setCountry(weChatUserInfoVO.getCountry())
        .setProvince(weChatUserInfoVO.getProvince())
        .setGender(weChatUserInfoVO.getSex())
        .setNickname(weChatUserInfoVO.getNickname())
        .setName(weChatUserInfoVO.getNickname())
        .setOpenid(weChatUserInfoVO.getOpenid())
        .setSignType(SignType.SCAN_CODE)
        .setSpeakStatus(Status.ENABLE)
        .setCompanyId(companyId)
        .setCreateTime(now)
        .setUpdateTime(now)
        .setStatus(Status.ENABLE);
    personDao.insert(person);

    SignInPersonVO signInPersonVO = BeanUtil.copyProperties(person, SignInPersonVO.class)
        .setSignInTime(now).setCompanyName(company.getName());

    // 推送签到消息
    sendSignInSuccessMessage(signInPersonVO, companyId);

    return ResultUtil.success(signInPersonVO);
  }

  /**
   * 推送签到消息
   *
   * @param signInPersonVO 签到人员信息
   */
  private void sendSignInSuccessMessage(SignInPersonVO signInPersonVO, Long companyId) {
    if (signInPersonVO != null && companyId != null) {
      CompletableFuture.runAsync(() -> {
        WebSocketMessageVO<List<SignInPersonVO>> webSocketMessageVO = new WebSocketMessageVO<>(
            WebSocketMessageType.SIGN_IN, Arrays.asList(signInPersonVO));
        serverManageWebSocket.sendMessageAll(JsonUtil.obj2String(webSocketMessageVO), companyId);
      });
    }
  }

  @Override
  public RespMsg search(PersonSearchVO personSearchVO) {
    List<PersonVO> list = personDao.search(personSearchVO);
    return ResultUtil.success(list);
  }

  @Transactional
  @Override
  public RespMsg banned(PersonSpeakStatusVO personSpeakStatusVO) {
    Long id = personSpeakStatusVO.getId();
    Integer speakStatus = personSpeakStatusVO.getSpeakStatus();
    Long companyId = personSpeakStatusVO.getCompanyId();
    if (id == null || speakStatus == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    Person person = BeanUtil.copyProperties(personSpeakStatusVO, Person.class);
    person.setUpdateTime(new Date());
    int rows = personDao.updateSelective(person);
    if (rows == 0) {
      return ResultUtil.error(ErrCode.PERSON_BANNED_ERROR);
    }

    if (Status.DISABLE == speakStatus) {
      redisUtil.sSet(RedisKey.BANNED_PERSON_ID + Constant.SPLITTER_COLON + companyId, id);
    } else {
      redisUtil.sRemove(RedisKey.BANNED_PERSON_ID + Constant.SPLITTER_COLON + companyId, id);
    }
    return ResultUtil.success();
  }

  @Override
  public RespMsg noLottery(Long companyId) {
    List<Person> noLotteryPeople = personDao.findNoLotteryPeople(companyId);
    List<PersonNoLotteryVO> list = BeanUtil
        .copyProperties(Optional.ofNullable(noLotteryPeople).orElse(Collections.emptyList()),
            PersonNoLotteryVO.class);
    return ResultUtil.success(list);
  }

  @Transactional
  @Override
  public RespMsg delete(Long id) {
    Person person = new Person();
    person.setId(id).setUpdateTime(new Date()).setStatus(Status.DISABLE);
    int rows = personDao.updateSelective(person);
    if (rows == 0) {
      return ResultUtil.error(ErrCode.PERSON_DELETE_ERROR);
    }
    return ResultUtil.success();
  }

  @Transactional
  @Override
  public RespMsg add(PersonEntryVO personEntryVO) {
    Date now = new Date();

    // 查询缓存中的企业
    Long companyId = personEntryVO.getCompanyId();
    Company company = redisUtil.hGet(RedisKey.COMPANY_MAP, companyId, Company.class);
    if (company == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    String cardNumber = personEntryVO.getCardNumber();
    String name = personEntryVO.getName();
    if (StringUtils.isEmpty(cardNumber) || StringUtils.isEmpty(name)) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    // 验证身份证合法性
    if (!IdcardUtil.isValidatedAllIdcard(cardNumber)) {
      return ResultUtil.error(ErrCode.PERSON_CARD_NUMBER_ERROR);
    }

    long count = personDao
        .countByExample(new PersonExampleVO().setCardNumber(cardNumber).setCompanyId(companyId));
    if (count != 0) {
      return ResultUtil.error(ErrCode.HAD_SIGN_IN);
    }

    // 设置性别和头像
    Integer gender = IdcardUtil.getGenderFromIdcard(cardNumber);
    String avatarUrl = Constant.AVATAR_MEN;
    if (gender == Constant.GENDER_WOMEN) {
      avatarUrl = Constant.AVATAR_WOMEN;
    }

    Person person = new Person();
    person.setNickname(name).setName(name).setAvatarUrl(avatarUrl).setGender(gender)
        .setCardNumber(cardNumber).setSignType(SignType.MANUAL_ENTRY).setSpeakStatus(Status.ENABLE)
        .setCompanyId(companyId).setStatus(Status.ENABLE).setCreateTime(now)
        .setUpdateTime(now);

    personDao.insert(person);

    SignInPersonVO signInPersonVO = BeanUtil.copyProperties(person, SignInPersonVO.class)
        .setSignInTime(now).setCompanyName(company.getName());

    // 推送签到消息
    sendSignInSuccessMessage(signInPersonVO, companyId);

    return ResultUtil.success();
  }

}
