package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ShowVote extends BaseEntity {

  private static final long serialVersionUID = -8248443864971827213L;

  private Long personId;

  private Long showId;

}
