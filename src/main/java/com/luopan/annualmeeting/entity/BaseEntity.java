package com.luopan.annualmeeting.entity;

import com.luopan.annualmeeting.common.Constant.Status;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class BaseEntity implements Serializable {

  private Long id;

  private Date createTime;

  private Date updateTime;

  private Integer status;

  private Long companyId;

  public void fillDefaultProperty() {
    this.createTime = new Date();
    this.updateTime = new Date();
    this.status = Status.ENABLE;
  }

}
