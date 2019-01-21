package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/16.
 */
@Data
@Accessors(chain = true)
public class CompanyVO implements Serializable {

  private static final long serialVersionUID = -3487812703446866609L;

  private Long id;

  private String name;

}
