package com.luopan.annualmeeting.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/16.
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseEntity {

  private static final long serialVersionUID = 1464139308162247152L;

  private String name;

  private String wechatAppId;

  private String wechatAppSecret;

}
