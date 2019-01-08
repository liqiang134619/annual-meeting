package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonSearchVO implements Serializable {

  private static final long serialVersionUID = -1391115049666600073L;
  
  private String name;
  
  private String speakStatus;
  
}
