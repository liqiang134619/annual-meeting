package com.luopan.annualmeeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatAuthVO implements Serializable {

  private static final long serialVersionUID = 2652791896473964322L;

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private String expiresIn;

  @JsonProperty("refresh_token")
  private String refreshToken;

  private String openid;

  private String scope;

}
