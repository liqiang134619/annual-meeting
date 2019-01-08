package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LotteryRewardVO implements Serializable {

  private static final long serialVersionUID = -7157904358218880590L;

  private String name;

  private String imageUrl;

  private Integer level;

}
