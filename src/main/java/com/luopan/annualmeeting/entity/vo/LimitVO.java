package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/10.
 */
@Data
@Accessors(chain = true)
public class LimitVO implements Serializable {

  private static final long serialVersionUID = -6523251792700666155L;

  private Long offset;

  private Integer size;

  private Long companyId;

}
