package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ShowPerson extends BaseEntity {

  private static final long serialVersionUID = 7205232129708524578L;

  // 节目id
  private Long showId;

  // 人员id
  private Long personId;

}
