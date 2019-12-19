package com.luopan.annualmeeting.task;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 整合配置spring与quartz
 * @author Administrator
 */
public class MyJobFactory extends AdaptableJobFactory {

  /**
   * spring context
   */
  @Autowired
  private AutowireCapableBeanFactory capableBeanFactory;

  /**
   * create a jon instance , add Spring injection
   * @param bundle A simple class (structure) used for returning execution-time data from the JobStore to the QuartzSchdulerThread
   * @return
   * @throws Exception
   */
  @Override
  protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
    Object jobInstance = super.createJobInstance(bundle);
    capableBeanFactory.autowireBean(jobInstance);
    return jobInstance;
  }
}
