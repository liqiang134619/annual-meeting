package com.luopan.annualmeeting.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/24.
 */
@Data
@Accessors(chain = true)
public class PersonEntryVO {

  private String cardNumber;

  private String name;

  private Long companyId;

}
