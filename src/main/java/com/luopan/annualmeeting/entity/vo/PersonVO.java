package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonVO implements Serializable {

  private static final long serialVersionUID = -524736578132206644L;

  private Long id;

  private String name;

  private String nickname;

  private String avatarUrl;

  private Integer gender;

  private String phone;

  private String country;

  private String province;

  private String city;

  private Integer signType;

  private Integer speakStatus;

  private Date signTime;

}
