package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CountVO implements Serializable {

  private static final long serialVersionUID = -7071920968153480900L;

  private Long signNum;

  private Long messageNum;

  private Integer taskInterval;

  private Integer taskMessageNum;

  private Integer personVoteNum;

  private Date voteStartTime;

  private Date voteEndTime;

}
