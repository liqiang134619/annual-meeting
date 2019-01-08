package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShowScorePraiseCommentVO implements Serializable {

  private static final long serialVersionUID = 754782151235759882L;

  private Long id;

  private String name;

  private String department;

  private String performer;

  private String type;

  private Integer orderNum;

  private Integer isPraise;

  private Integer isScore;

  private Integer score;

  private Integer praiseNum;

  private Integer commentNum;

  private Double avgScore;

}
