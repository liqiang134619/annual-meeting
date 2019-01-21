package com.luopan.annualmeeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WeChatUserInfoVO implements Serializable {

  private static final long serialVersionUID = 5462301491913601255L;

  private Integer subscribe;

  private String openid;
  
  private String nickname;
  
  private Integer sex;

  private String city;
  
  private String province;
  
  private String country;

  @JsonProperty("headimgurl")
  private String headImgUrl;

}
