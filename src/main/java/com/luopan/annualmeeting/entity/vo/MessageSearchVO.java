package com.luopan.annualmeeting.entity.vo;

import com.luopan.annualmeeting.util.DateUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Accessors(chain = true)
public class MessageSearchVO implements Serializable {

  private static final long serialVersionUID = -3109264736890141987L;

  private String personName;

  @DateTimeFormat(pattern = DateUtil.DATE_TIME_PATTERN)
  private Date startTime;

  @DateTimeFormat(pattern = DateUtil.DATE_TIME_PATTERN)
  private Date endTime;

  private Integer checkStatus;

  private Long companyId;

}
