package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/16.
 */
@Data
@Accessors(chain = true)
public class UserCompanyVO implements Serializable {

  private static final long serialVersionUID = 5771302726152109545L;

  private Long companyId;

  private String companyName;

}
