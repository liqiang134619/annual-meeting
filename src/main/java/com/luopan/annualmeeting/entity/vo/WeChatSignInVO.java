package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatSignInVO implements Serializable {

  private static final long serialVersionUID = -2353865572366993093L;

  private String code;

  private String name;

  private String phone;

  private String openid;

  private Long companyId;

}
