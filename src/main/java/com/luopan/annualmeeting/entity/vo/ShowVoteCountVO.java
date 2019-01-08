package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShowVoteCountVO implements Serializable {

  private static final long serialVersionUID = -2300503620731495645L;

  private Long id;

  private String name;

  private String department;

  private String performer;

  private String type;

  private Integer orderNum;

  private Integer voteCount;

}
