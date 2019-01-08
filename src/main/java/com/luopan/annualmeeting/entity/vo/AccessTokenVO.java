package com.luopan.annualmeeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccessTokenVO implements Serializable {

  private static final long serialVersionUID = 849222109636589199L;

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("expires_in")
  private Long expiresIn;

}
