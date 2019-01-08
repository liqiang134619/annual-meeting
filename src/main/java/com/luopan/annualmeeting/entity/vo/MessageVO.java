package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageVO implements Serializable {

  private static final long serialVersionUID = -5055257300862267660L;

  private Long id;

  private String message;

  private Date pubTime;

  private String name;

  private String nickname;

  private String avatarUrl;

  private Integer isTop;

}
