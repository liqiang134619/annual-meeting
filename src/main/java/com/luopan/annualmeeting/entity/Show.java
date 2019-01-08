package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Show extends BaseEntity {

  private static final long serialVersionUID = -7276637762696739449L;

  // 节目名
  private String name;

  // 部门名
  private String department;

  // 表演人
  private String performer;

  // 节目类型
  private String type;

  // 节目序号
  private Integer orderNum;

}
