package com.luopan.annualmeeting.runner;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.QuartzJobName;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.task.QuartzManager;
import com.luopan.annualmeeting.task.job.MessageJob;
import com.luopan.annualmeeting.task.job.ShowVoteJob;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by lujw on 2019/1/18.
 */
@Slf4j
@Component
public class QuartzJobStartRunner implements CommandLineRunner {

  @Autowired
  private QuartzManager quartzManager;

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public void run(String... args) throws Exception {
    Set<Long> companyIds = redisUtil.hKeys(RedisKey.COMPANY_MAP, Long.class);
    if (!BeanUtil.isEmpty(companyIds)) {
      companyIds.forEach(companyId -> {
        Integer messageTaskInterval = Optional.ofNullable(redisUtil
            .get(RedisKey.MESSAGE_TASK_INTERVAL + Constant.SPLITTER_COLON + companyId,
                Integer.class)).orElse(Constant.MESSAGE_TASK_INTERVAL);
        Boolean messageJobSuccess = quartzManager
            .newJob(QuartzJobName.MESSAGE + Constant.SPLITTER_COLON + companyId,
                MessageJob.class, messageTaskInterval);

        Boolean voteJobSuccess = quartzManager
            .newJob(QuartzJobName.VOTE + Constant.SPLITTER_COLON + companyId, ShowVoteJob.class,
                Constant.VOTE_TASK_INTERVAL);

        if (!messageJobSuccess || !voteJobSuccess) {
          throw new RuntimeException("初始化定时任务错误");
        }
      });
    }

    // 启动任务调度器
    quartzManager.startScheduler();
  }

}
