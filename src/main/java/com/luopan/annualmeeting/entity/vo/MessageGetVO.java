package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/17.
 */
@Data
@Accessors(chain = true)
public class MessageGetVO implements Serializable {

  private static final long serialVersionUID = -6691032825325946115L;

  private Long lastId;

  private String act;

  private Long companyId;

  private Integer num;

  private Long personId;

}
