package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebSocketMessageVO<T> implements Serializable {

  private static final long serialVersionUID = 152479072217802193L;

  private Integer type;

  private T data;

}
