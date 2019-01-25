package com.luopan.annualmeeting.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/24.
 * 罗盘扫后台二维码绑定
 */
@Data
@Accessors(chain = true)
public class PersonBindingVO {

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

  private Long companyId;

  private String cardNumberLastSix;

}
