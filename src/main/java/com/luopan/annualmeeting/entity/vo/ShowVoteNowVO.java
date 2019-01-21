package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/17.
 */
@Data
@Accessors(chain = true)
public class ShowVoteNowVO implements Serializable {

  private static final long serialVersionUID = 70286107037034913L;

  private Long showId;

  private Long companyId;

  private Integer voteAble;

}
