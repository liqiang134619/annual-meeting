package com.luopan.annualmeeting.task;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Slf4j
@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private QuartzManager quartzManager;

  @Autowired
  private MyJobFactory myJobFactory;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    try {
      quartzManager.startJob();
    } catch (SchedulerException e) {
      log.error("quartz任务启动错误", e);
    }
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    schedulerFactoryBean.setJobFactory(myJobFactory);
    return schedulerFactoryBean;
  }

  @Bean
  public Scheduler scheduler() {
    return schedulerFactoryBean().getScheduler();
  }

}
