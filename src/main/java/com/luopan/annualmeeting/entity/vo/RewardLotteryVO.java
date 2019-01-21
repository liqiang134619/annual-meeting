package com.luopan.annualmeeting.entity.vo;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/21.
 */
@Data
@Accessors(chain = true)
public class RewardLotteryVO {

  private Long rewardId;

  private List<Long> personIds;

  private Long companyId;

}
