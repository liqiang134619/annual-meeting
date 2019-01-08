package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatOpenidVO implements Serializable {

  private static final long serialVersionUID = -741085920348201951L;

  private String openid;

}
