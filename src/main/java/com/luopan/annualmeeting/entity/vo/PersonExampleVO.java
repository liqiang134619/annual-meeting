package com.luopan.annualmeeting.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/23.
 */
@Data
@Accessors(chain = true)
public class PersonExampleVO {

  private Long companyId;

  private String cardNumber;

  // 身份证后六位
  private String cardNumberLastSix;

  private String openid;

}
