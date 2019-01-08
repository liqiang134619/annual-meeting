package com.luopan.annualmeeting.task;

import com.luopan.annualmeeting.common.Constant.QuartzJobName;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.config.CommConfig;
import com.luopan.annualmeeting.task.job.MessageJob;
import com.luopan.annualmeeting.task.job.ShowVoteJob;
import com.luopan.annualmeeting.util.DateUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.Tools;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuartzManager {

  @Autowired
  private Scheduler scheduler;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private CommConfig commConfig;

  public void startJob() throws SchedulerException {
    startMessageJob(scheduler);
    startVoteJob(scheduler);
    scheduler.start();
  }

  /**
   * 获取任务间隔
   */
  private int getMessageJobInterval() {
    return Tools.getInt(redisUtil.getString(RedisKey.MESSAGE_TASK_INTERVAL),
        commConfig.getMessageTaskInterval());
  }

  /**
   * 启动任务
   */
  private void startMessageJob(Scheduler scheduler) throws SchedulerException {
    // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
    // JobDetail 是具体Job实例
    JobDetail jobDetail = JobBuilder.newJob(MessageJob.class).withIdentity(QuartzJobName.MESSAGE)
        .build();
    SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
        .repeatSecondlyForever(getMessageJobInterval());
    // TriggerBuilder 用于构建触发器实例
    SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
        .withIdentity(QuartzJobName.MESSAGE)
        .withSchedule(simpleScheduleBuilder).build();
    scheduler.scheduleJob(jobDetail, simpleTrigger);
  }

  /**
   * 启动任务
   * @param scheduler
   * @throws SchedulerException
   */
  private void startVoteJob(Scheduler scheduler) throws SchedulerException {
    Date startTime = (Date) redisUtil.get(RedisKey.VOTE_START_TIME);
    if (startTime != null) {
      JobDetail jobDetail = JobBuilder.newJob(ShowVoteJob.class).withIdentity(QuartzJobName.VOTE)
          .build();
      SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
          .repeatSecondlyForever(commConfig.getVoteTaskInterval());
      TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder.newTrigger()
          .withIdentity(QuartzJobName.VOTE).withSchedule(simpleScheduleBuilder)
          .startAt(startTime);
      Date endTime = (Date) redisUtil.get(RedisKey.VOTE_END_TIME);
      if (endTime != null) {
        triggerBuilder.endAt(DateUtil.plusSecond(endTime, commConfig.getVoteTaskInterval()));
      }
      SimpleTrigger simpleTrigger = triggerBuilder.build();
      scheduler.scheduleJob(jobDetail, simpleTrigger);
    }
  }

  /**
   * 修改任务
   */
  public boolean modifyMessageJob(int newInterval) {
    try {
      Date date = null;
      TriggerKey triggerKey = new TriggerKey(QuartzJobName.MESSAGE);
      SimpleTrigger simpleTrigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
      long oldInterval = simpleTrigger.getRepeatInterval();
      if (oldInterval != newInterval) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder
            .repeatSecondlyForever(newInterval);
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(QuartzJobName.MESSAGE)
            .withSchedule(builder).build();
        date = scheduler.rescheduleJob(triggerKey, trigger);
      }
      return date != null;
    } catch (SchedulerException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 保存任务
   *
   * @param jobName 任务名
   * @param jobClass 任务类型
   * @param interval 任务间隔
   * @param endDate 任务结束时间
   */
  public void saveJob(String jobName, Class<? extends Job> jobClass, int interval, Date endDate) {
    TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
    SimpleScheduleBuilder builder = SimpleScheduleBuilder.repeatSecondlyForever(interval);
    try {
      SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger(triggerKey);
      if (trigger == null) {
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName).build();
        TriggerBuilder<SimpleTrigger> triggerBuilder = TriggerBuilder.newTrigger()
            .withIdentity(triggerKey).withSchedule(builder);
        if (endDate != null) {
          triggerBuilder.endAt(DateUtil.plusSecond(endDate, interval));
        }
        trigger = triggerBuilder.build();
        scheduler.scheduleJob(jobDetail, trigger);
      } else {
        TriggerBuilder<SimpleTrigger> triggerBuilder = trigger.getTriggerBuilder()
            .withIdentity(triggerKey).withSchedule(builder);
        if (endDate != null) {
          triggerBuilder.endAt(DateUtil.plusSecond(endDate, interval));
        }
        trigger = triggerBuilder.build();
        scheduler.rescheduleJob(triggerKey, trigger);
      }
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }

  /**
   * 保存任务
   *
   * @param jobName 任务名
   * @param jobClass 任务类型
   * @param interval 任务间隔
   */
  public void saveJob(String jobName, Class<? extends Job> jobClass, int interval) {
    saveJob(jobName, jobClass, interval, null);
  }

}
