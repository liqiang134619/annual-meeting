package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Praise extends BaseEntity {

  private static final long serialVersionUID = 3372983402319424150L;

  // 节目id
  private Long showId;

  // 人员id
  private Long personId;

}
