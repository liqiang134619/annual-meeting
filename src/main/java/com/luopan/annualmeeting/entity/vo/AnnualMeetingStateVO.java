package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AnnualMeetingStateVO implements Serializable {

  private static final long serialVersionUID = 6618772059537987926L;

  private Date startTime;

  private Date endTime;

}
