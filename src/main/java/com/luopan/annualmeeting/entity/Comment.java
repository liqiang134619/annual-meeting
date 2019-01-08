package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity {

  private static final long serialVersionUID = 2027696060574546791L;

  // 评论
  private String comment;

  // 节目id
  private Long showId;

  // 人员id
  private Long personId;

}
