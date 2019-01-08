package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageTopVO implements Serializable {

  private static final long serialVersionUID = 8977381365711133339L;

  private Long id;

  private Integer isTop;

}
