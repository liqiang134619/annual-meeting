package com.luopan.annualmeeting.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/21.
 */
@Data
@Accessors(chain = true)
public class PersonFaceSignInVO {

  private String name;

  private String avatarUrl;

  private Integer gender;

  private String cardNumber;

}
