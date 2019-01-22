package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.QuartzJobName;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.vo.AnnualMeetingStateVO;
import com.luopan.annualmeeting.service.IAnnualMeetingService;
import com.luopan.annualmeeting.task.QuartzManager;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnualMeetingService implements IAnnualMeetingService {

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private QuartzManager quartzManager;

  @Override
  public RespMsg setState(AnnualMeetingStateVO annualMeetingStateVO) {
    // redis命令
    Map<String, Object> pipCommand = new HashMap<>();
    Long companyId = annualMeetingStateVO.getCompanyId();

    // 设置年会配置
    String annualMeetingStateKey =
        RedisKey.ANNUAL_MEETING_STATE + Constant.SPLITTER_COLON + companyId;
    AnnualMeetingStateVO vo = Optional
        .ofNullable(redisUtil.get(annualMeetingStateKey, AnnualMeetingStateVO.class))
        .orElse(new AnnualMeetingStateVO());
    AnnualMeetingStateVO oldVo = BeanUtil.copyProperties(vo, AnnualMeetingStateVO.class);
    Date startTime = annualMeetingStateVO.getStartTime();
    if (startTime != null) {
      vo.setStartTime(startTime);
    }
    Date endTime = annualMeetingStateVO.getEndTime();
    if (endTime != null) {
      vo.setEndTime(endTime);
    }
    String title = annualMeetingStateVO.getTitle();
    if (title != null) {
      vo.setTitle(title);
    }
    String webUrl = annualMeetingStateVO.getWebUrl();
    if (webUrl != null) {
      vo.setWebUrl(webUrl);
    }
    String mobileUrl = annualMeetingStateVO.getMobileUrl();
    if (mobileUrl != null) {
      vo.setMobileUrl(mobileUrl);
    }
    String backgroundImageUrl = annualMeetingStateVO.getBackgroundImageUrl();
    if (backgroundImageUrl != null) {
      vo.setBackgroundImageUrl(backgroundImageUrl);
    }
    String rewardBackgroundImageUrl = annualMeetingStateVO.getRewardBackgroundImageUrl();
    if (rewardBackgroundImageUrl != null) {
      vo.setRewardBackgroundImageUrl(rewardBackgroundImageUrl);
    }
    if (!oldVo.equals(vo)) {
      pipCommand.put(annualMeetingStateKey, vo);
    }

    // 修改消息推送间隔
    Integer interval = annualMeetingStateVO.getMessageTaskInterval();
    if (interval != null && interval > 0) {
      String messageTaskIntervalKey =
          RedisKey.MESSAGE_TASK_INTERVAL + Constant.SPLITTER_COLON + companyId;
      Integer messageTaskInterval = Optional
          .ofNullable(redisUtil.get(messageTaskIntervalKey, Integer.class))
          .orElse(Constant.MESSAGE_TASK_INTERVAL);
      if (!interval.equals(messageTaskInterval)) {
        boolean success = quartzManager
            .updateJob(QuartzJobName.MESSAGE + Constant.SPLITTER_COLON + companyId, interval);
        if (!success) {
          return ResultUtil.error(ErrCode.TASK_MODIFY_INTERVAL_ERROR);
        }
        pipCommand.put(messageTaskIntervalKey, interval);
      }
    }

    // 修改消息推送数
    Integer num = annualMeetingStateVO.getMessageTaskNum();
    if (num != null) {
      String messageTaskNumKey =
          RedisKey.MESSAGE_TASK_NUM + Constant.SPLITTER_COLON + companyId;
      Integer messageTaskNum = Optional.ofNullable(redisUtil.get(messageTaskNumKey, Integer.class))
          .orElse(Constant.MESSAGE_TASK_NUM);
      if (!num.equals(messageTaskNum)) {
        pipCommand.put(messageTaskNumKey, num);
      }
    }

    // 设置消息审批状态
    Integer messageCheckState = annualMeetingStateVO.getMessageCheckState();
    if (messageCheckState != null) {
      String checkStateKey = RedisKey.MESSAGE_CHECK_STATE + Constant.SPLITTER_COLON + companyId;
      Integer oldCheckState = Optional.ofNullable(redisUtil.get(checkStateKey, Integer.class))
          .orElse(Constant.MESSAGE_CHECK_STATUS);
      if (!oldCheckState.equals(messageCheckState)) {
        pipCommand.put(checkStateKey, messageCheckState);
      }
    }

    // 修改单人投票次数
    Integer personVoteNum = annualMeetingStateVO.getPersonVoteNum();
    if (personVoteNum != null) {
      String personVoteNumKey = RedisKey.PERSON_VOTE_NUM + Constant.SPLITTER_COLON + companyId;
      Integer oldPersonVoteNum = Optional.ofNullable(redisUtil.get(personVoteNumKey, Integer.class))
          .orElse(Constant.PERSON_VOTE_NUM);
      if (!oldPersonVoteNum.equals(personVoteNum)) {
        pipCommand.put(personVoteNumKey, personVoteNum);
      }
    }

    // 同步修改到redis
    redisUtil.pipSet(pipCommand);
    return ResultUtil.success();
  }

  @Override
  public RespMsg getState(Long companyId) {
    String annualMeetingStateKey =
        RedisKey.ANNUAL_MEETING_STATE + Constant.SPLITTER_COLON + companyId;
    AnnualMeetingStateVO annualMeetingStateVO = Optional.ofNullable(redisUtil
        .get(annualMeetingStateKey, AnnualMeetingStateVO.class)).orElse(new AnnualMeetingStateVO());
    if (annualMeetingStateVO.getCompanyId() == null) {
      annualMeetingStateVO.setCompanyId(companyId);
    }

    String messageTaskIntervalKey =
        RedisKey.MESSAGE_TASK_INTERVAL + Constant.SPLITTER_COLON + companyId;
    Integer messageTaskInterval = Optional
        .ofNullable(redisUtil.get(messageTaskIntervalKey, Integer.class))
        .orElse(Constant.MESSAGE_TASK_INTERVAL);

    String messageTaskNumKey =
        RedisKey.MESSAGE_TASK_NUM + Constant.SPLITTER_COLON + companyId;
    Integer messageTaskNum = Optional.ofNullable(redisUtil.get(messageTaskNumKey, Integer.class))
        .orElse(Constant.MESSAGE_TASK_NUM);

    String checkStateKey = RedisKey.MESSAGE_CHECK_STATE + Constant.SPLITTER_COLON + companyId;
    Integer checkState = Optional.ofNullable(redisUtil.get(checkStateKey, Integer.class))
        .orElse(Constant.MESSAGE_CHECK_STATUS);

    String personVoteNumKey = RedisKey.PERSON_VOTE_NUM + Constant.SPLITTER_COLON + companyId;
    Integer personVoteNum = Optional.ofNullable(redisUtil.get(personVoteNumKey, Integer.class))
        .orElse(Constant.PERSON_VOTE_NUM);

    annualMeetingStateVO.setPersonVoteNum(personVoteNum).setMessageCheckState(checkState)
        .setMessageTaskNum(messageTaskNum).setMessageTaskInterval(messageTaskInterval);

    return ResultUtil.success(annualMeetingStateVO);
  }
}
