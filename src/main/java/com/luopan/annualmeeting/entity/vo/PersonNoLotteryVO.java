package com.luopan.annualmeeting.entity.vo;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lujw on 2019/1/21.
 */
@Data
@Accessors(chain = true)
public class PersonNoLotteryVO implements Serializable {

  private static final long serialVersionUID = 4657591391445706283L;

  private Long id;

  private String name;

  private String nickname;

  private String avatarUrl;

  private Integer gender;

  private String phone;

  private String country;

  private String province;

  private String city;

  private Integer lotteryNumber;

}
