package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Lottery extends BaseEntity {

  private static final long serialVersionUID = 3436780024319532824L;
  // 人员id
  private Long personId;

  // 奖品id
  private Long rewardId;

}
