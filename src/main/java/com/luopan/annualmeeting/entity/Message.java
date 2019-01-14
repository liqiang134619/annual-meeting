package com.luopan.annualmeeting.entity;

import com.luopan.annualmeeting.common.Constant.Status;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseEntity {

  private static final long serialVersionUID = -8249945832841084946L;

  // 留言
  private String message;

  // 人员id
  private Long personId;

  // 审核状态(0待审核，1通过，2驳回)
  private Integer checkStatus;

  // 审核时间
  private Date checkTime;

  // 是否置顶
  private Integer isTop;

  // 置顶时间
  private Date topTime;

  @Override
  public void fillDefaultProperty() {
    super.fillDefaultProperty();
    this.setIsTop(Status.DISABLE);
  }
}
