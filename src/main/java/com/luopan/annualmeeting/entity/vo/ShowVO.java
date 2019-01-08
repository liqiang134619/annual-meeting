package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShowVO implements Serializable {

  private static final long serialVersionUID = 5753079106665482192L;

  private Long id;

  private String name;

  private String department;

  private String performer;

  private String type;

  private Integer orderNum;

}
