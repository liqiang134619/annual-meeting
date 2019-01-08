package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageCheckVO implements Serializable {

  private static final long serialVersionUID = -5722194781085691059L;

  private Long id;

  private Integer checkStatus;

}
