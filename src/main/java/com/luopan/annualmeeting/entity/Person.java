package com.luopan.annualmeeting.entity;

import com.luopan.annualmeeting.common.Constant.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends BaseEntity {

  private static final long serialVersionUID = -2659927639396271344L;

  // 姓名
  private String name;

  // 昵称
  private String nickname;

  // 头像
  private String avatarUrl;

  // 性别 1男 2女
  private Integer gender;

  // 手机号
  private String phone;

  // 国家
  private String country;

  // 省份
  private String province;

  // 城市
  private String city;

  // 签到类型 1扫码 2人脸识别
  private Integer signType;

  // 微信openid
  private String openid;

  // 发言状态 1正常 0禁言
  private Integer speakStatus;

  @Override
  public void fillDefaultProperty() {
    super.fillDefaultProperty();
    this.setSpeakStatus(Status.ENABLE);
  }
}
