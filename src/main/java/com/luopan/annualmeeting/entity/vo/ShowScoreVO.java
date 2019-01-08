package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShowScoreVO implements Serializable {

  private static final long serialVersionUID = -8696052443416031642L;

  private Integer score;

  private Long personId;

  private Long showId;

}
