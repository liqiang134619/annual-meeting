package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PersonSpeakStatusVO implements Serializable {

  private static final long serialVersionUID = -175776670820730443L;

  private Long id;

  private Integer speakStatus;

  private Long companyId;

}
