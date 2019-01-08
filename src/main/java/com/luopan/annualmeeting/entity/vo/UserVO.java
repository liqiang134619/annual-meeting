package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVO implements Serializable {

  private static final long serialVersionUID = 1883929652173275748L;

  private String username;

  private String password;

}
