package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LotteryPersonRewardVO implements Serializable {

  private static final long serialVersionUID = -6782239686812564766L;

  private String name;

  private String nickname;

  private String avatarUrl;

  private Integer gender;

  private String country;

  private String province;

  private String city;

  private String rewardName;

  private Integer rewardLevel;

  private String rewardImageUrl;

}
