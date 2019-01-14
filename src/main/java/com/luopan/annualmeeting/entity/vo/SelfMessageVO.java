package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/10.
 */
@Data
@Accessors(chain = true)
public class SelfMessageVO implements Serializable {

  private static final long serialVersionUID = -525423042156841936L;

  private Long id;

  private String message;

  private Date pubTime;

}
