package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentPersonVO implements Serializable {

  private static final long serialVersionUID = 418793152222510925L;

  private String comment;

  private String name;

  private String avatarUrl;

  private String nickname;

  private Date commentTime;

}
