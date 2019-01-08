package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageTaskVO implements Serializable {

  private static final long serialVersionUID = -4787548851419204488L;

  private Integer interval;

  private Integer num;

}
