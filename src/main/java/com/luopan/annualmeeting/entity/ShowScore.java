package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ShowScore extends BaseEntity {

  private static final long serialVersionUID = -3853989899097861564L;

  // 节目id
  private Long showId;

  // 人员id
  private Long personId;

  // 评分
  private Integer score;

}
