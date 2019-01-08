package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonIdVO implements Serializable {

  private static final long serialVersionUID = -5032559379707180987L;

  private Long personId;

}
