package com.luopan.annualmeeting.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CommentVO implements Serializable {

  private static final long serialVersionUID = -391091749371134701L;

  @ApiModelProperty("节目ID")
  private Long showId;

  @ApiModelProperty("人员ID")
  private Long personId;

  @ApiModelProperty("评论内容")
  private String comment;

}
