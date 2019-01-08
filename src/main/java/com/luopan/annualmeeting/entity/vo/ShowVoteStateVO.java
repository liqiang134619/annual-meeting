package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShowVoteStateVO implements Serializable {

  private static final long serialVersionUID = -1791387556604286338L;

  private Date startTime;

  private Date endTime;

  private Integer personVoteNum;

}
