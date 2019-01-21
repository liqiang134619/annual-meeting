package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/18
 * 发送留言
 */
@Data
@Accessors(chain = true)
public class MessageSendVO implements Serializable {

  private static final long serialVersionUID = -1842836562313111822L;

  private String message;

  private Long personId;

  private Long companyId;

}
