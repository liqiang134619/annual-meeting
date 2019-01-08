package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Reward extends BaseEntity {

  private static final long serialVersionUID = -1178511031467438504L;

  // 奖品名
  private String name;

  // 奖品等级
  private Integer level;

  // 奖品数量
  private Integer amount;

  // 奖品图片
  private String imageUrl;

}
