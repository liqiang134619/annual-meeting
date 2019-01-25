package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatCodeVO implements Serializable {

  private static final long serialVersionUID = -2215039708190810097L;

  private String code;

  private Long companyId;

  private String cardNumber;

  private Integer lotteryNumber;

}
