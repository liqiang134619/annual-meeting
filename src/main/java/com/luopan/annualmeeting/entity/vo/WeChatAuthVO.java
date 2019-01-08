package com.luopan.annualmeeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatAuthVO implements Serializable {

  private static final long serialVersionUID = 2652791896473964322L;

  @JsonProperty("accessToken")
  private String access_token;

  @JsonProperty("expiresIn")
  private String expires_in;

  @JsonProperty("refreshToken")
  private String refresh_token;

  private String openid;

  private String scope;

}
