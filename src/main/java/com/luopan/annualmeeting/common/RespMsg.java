package com.luopan.annualmeeting.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * 通用返回消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespMsg<T> implements Serializable {

  private static final long serialVersionUID = 7503133101609340852L;

  private int errCode;

  private String errMsg;

  private T data;

}
