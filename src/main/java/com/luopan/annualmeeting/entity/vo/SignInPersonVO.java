package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 已签到人员
 */
@Data
@Accessors(chain = true)
public class SignInPersonVO implements Serializable {

  private static final long serialVersionUID = -8422426335668030778L;

  private Long id;

  private String name;

  private String nickname;

  private String avatarUrl;

  private Integer gender;

  private String country;

  private String province;

  private String city;

  private Date signInTime;

}
