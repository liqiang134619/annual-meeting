package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PraiseVO implements Serializable {

  private static final long serialVersionUID = -3689289450629350978L;

  private Long showId;

  private Long personId;

}
