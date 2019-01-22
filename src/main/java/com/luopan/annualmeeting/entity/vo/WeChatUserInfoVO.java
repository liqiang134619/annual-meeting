package com.luopan.annualmeeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatUserInfoVO implements Serializable {

  private static final long serialVersionUID = 5462301491913601255L;

  // 用户的唯一标识
  private String openid;

  // 昵称
  private String nickname;

  // 性别 （0未知 1男性 2女性）
  private Integer sex;

  // 城市
  private String city;

  // 省份
  private String province;

  // 国家
  private String country;

  // 用户头像
  private String headimgurl;

  private String unionid;

  // 用户特权信息
  private List<String> privilege;

}
