package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LotteryPersonVO implements Serializable {

  private static final long serialVersionUID = -4453156728319314084L;

  private String name;

  private String nickname;

  private String avatarUrl;

  private Integer gender;

  private String country;

  private String province;

  private String city;

}
