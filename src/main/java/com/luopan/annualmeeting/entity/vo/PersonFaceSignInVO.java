package com.luopan.annualmeeting.entity.vo;

import com.luopan.annualmeeting.util.DateUtil;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by lujw on 2019/1/21.
 */
@Data
@Accessors(chain = true)
public class PersonFaceSignInVO {

  private String name;

  private String cardNumber;

  @DateTimeFormat(pattern = DateUtil.DATE_TIME_PATTERN)
  private Date enterTime;

}
