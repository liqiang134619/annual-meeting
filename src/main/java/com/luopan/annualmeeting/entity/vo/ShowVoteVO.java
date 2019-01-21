package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShowVoteVO implements Serializable {

  private static final long serialVersionUID = 731154079393766083L;

  private Long personId;

  private Long showId;

  private Long companyId;

}
