package com.luopan.annualmeeting.task;

import com.luopan.annualmeeting.util.BeanUtil;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class QuartzManager {

  @Autowired
  private Scheduler scheduler;

  public void startScheduler() throws SchedulerException {
    scheduler.start();
  }

  /**
   * 新建任务
   *
   * @param jobName 任务名
   * @param jobClass 任务类型
   * @param interval 任务间隔
   * @return true or false
   */
  public Boolean newJob(String jobName, Class<? extends Job> jobClass, int interval) {
    return newJob(jobName, jobClass, interval, null);
  }

  /**
   * 新建任务
   *
   * @param jobName 任务名
   * @param jobClass 任务类型
   * @param interval 任务间隔
   * @param params 参数
   * @return true or false
   */
  public Boolean newJob(String jobName, Class<? extends Job> jobClass, Integer interval,
      Map<String, Object> params) {
    if (StringUtils.isEmpty(jobName) || jobClass == null || interval == null || interval < 0) {
      return false;
    }
    JobBuilder jobBuilder = JobBuilder.newJob(jobClass).withIdentity(jobName);
    if (BeanUtil.isNotEmpty(params)) {
      jobBuilder.setJobData(map2JobDataMap(params));
    }
    JobDetail jobDetail = jobBuilder.build();
    SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
        .repeatSecondlyForever(interval);
    SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(jobName)
        .withSchedule(simpleScheduleBuilder).build();
    try {
      Date date = scheduler.scheduleJob(jobDetail, simpleTrigger);
      return date != null;
    } catch (SchedulerException e) {
      log.error("新建定时任务错误", e);
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 更新任务
   *
   * @param jobName 任务名
   * @param interval 间隔
   * @param params 参数
   * @return true or false
   */
  public Boolean updateJob(String jobName, Integer interval, Map<String, Object> params) {
    if (StringUtils.isEmpty(jobName) || interval == null || interval < 0) {
      return false;
    }
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
    try {
      SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
      if (trigger == null) {
        return false;
      }
      SimpleScheduleBuilder builder = SimpleScheduleBuilder.repeatSecondlyForever(interval);
      TriggerBuilder<SimpleTrigger> triggerBuilder = trigger.getTriggerBuilder()
          .withIdentity(triggerKey).withSchedule(builder);
      if (BeanUtil.isNotEmpty(params)) {
        triggerBuilder.usingJobData(map2JobDataMap(params));
      }
      trigger = triggerBuilder.build();
      Date date = scheduler.rescheduleJob(triggerKey, trigger);
      return date != null;
    } catch (SchedulerException e) {
      log.error("更新定时任务错误", e);
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 更新任务
   *
   * @param jobName 任务名
   * @param params 参数
   * @return true or false
   */
  public Boolean updateJob(String jobName, Map<String, Object> params) {
    if (StringUtils.isEmpty(jobName) || BeanUtil.isEmpty(params)) {
      return false;
    }
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
    try {
      SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
      if (trigger == null) {
        return false;
      }
      trigger = trigger.getTriggerBuilder()
          .usingJobData(map2JobDataMap(params)).build();
      Date date = scheduler.rescheduleJob(triggerKey, trigger);
      return date != null;
    } catch (SchedulerException e) {
      log.error("更新定时任务错误", e);
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 更新任务
   *
   * @param jobName 任务名
   * @param interval 间隔
   * @return true or false
   */
  public Boolean updateJob(String jobName, Integer interval) {
    return updateJob(jobName, interval, null);
  }

  /**
   * 删除任务
   *
   * @param jobName 任务名
   * @return true or false
   */
  public Boolean deleteJob(String jobName) {
    if (StringUtils.isEmpty(jobName)) {
      return false;
    }
    JobKey jobKey = new JobKey(jobName);
    try {
      if (!scheduler.checkExists(jobKey)) {
        return false;
      }
      return scheduler.deleteJob(jobKey);
    } catch (SchedulerException e) {
      log.error("删除定时任务错误", e);
      e.printStackTrace();
    }
    return false;
  }


  /**
   * 暂停任务
   *
   * @param jobName 任务名
   * @return true or false
   */
  public Boolean pauseJob(String jobName) {
    if (StringUtils.isEmpty(jobName)) {
      return false;
    }
    JobKey jobKey = new JobKey(jobName);
    try {
      if (!scheduler.checkExists(jobKey)) {
        return false;
      }
      scheduler.pauseJob(jobKey);
      return true;
    } catch (SchedulerException e) {
      log.error("暂停定时任务错误", e);
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 恢复任务
   *
   * @param jobName 任务名
   * @return true or false
   */
  public Boolean resumeJob(String jobName) {
    if (StringUtils.isEmpty(jobName)) {
      return false;
    }
    JobKey jobKey = new JobKey(jobName);
    try {
      if (!scheduler.checkExists(jobKey)) {
        return false;
      }
      scheduler.resumeJob(jobKey);
      return true;
    } catch (SchedulerException e) {
      log.error("恢复定时任务错误", e);
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 触发一次任务
   *
   * @param jobName 任务名
   * @return true or false
   */
  public Boolean executeOnce(String jobName) {
    if (StringUtils.isEmpty(jobName)) {
      return false;
    }
    JobKey jobKey = new JobKey(jobName);
    try {
      if (!scheduler.checkExists(jobKey)) {
        return false;
      }
      scheduler.triggerJob(jobKey);
      return true;
    } catch (SchedulerException e) {
      log.error("运行一次定时任务错误", e);
      e.printStackTrace();
    }
    return false;
  }

  private JobDataMap map2JobDataMap(Map<String, Object> params) {
    if (params == null) {
      return null;
    }
    JobDataMap jobDataMap = new JobDataMap();
    params.forEach(jobDataMap::put);
    return jobDataMap;
  }

}
