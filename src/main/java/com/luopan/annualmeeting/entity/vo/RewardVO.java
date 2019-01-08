package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RewardVO implements Serializable {

  private static final long serialVersionUID = 6822129275425174622L;

  private Long id;

  private Integer level;

  private Integer amount;

  private String name;

  private String imageUrl;

}
