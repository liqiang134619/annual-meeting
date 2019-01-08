package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant.QuartzJobName;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.config.CommConfig;
import com.luopan.annualmeeting.dao.ShowVoteDao;
import com.luopan.annualmeeting.entity.ShowVote;
import com.luopan.annualmeeting.entity.vo.ShowVoteStateVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteVO;
import com.luopan.annualmeeting.service.IShowVoteService;
import com.luopan.annualmeeting.task.QuartzManager;
import com.luopan.annualmeeting.task.job.ShowVoteJob;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import com.luopan.annualmeeting.util.Tools;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowVoteService implements IShowVoteService {

  @Autowired
  private ShowVoteDao showVoteDao;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private CommConfig commConfig;

  @Autowired
  private QuartzManager quartzManager;

  @Transactional
  @Override
  public RespMsg vote(ShowVoteVO showVoteVO) {
    Date nowTime = new Date();
    // 判断投票是否开始
    Date startTime = (Date) redisUtil.get(RedisKey.VOTE_START_TIME);
    if (startTime == null || startTime.compareTo(nowTime) >= 0) {
      return ResultUtil.error(ErrCode.VOTE_NOT_STARTED);
    }

    // 判断投票是否结束
    Date endTime = (Date) redisUtil.get(RedisKey.VOTE_END_TIME);
    if (endTime != null && nowTime.compareTo(endTime) >= 0) {
      return ResultUtil.error(ErrCode.VOTE_ENDED);
    }

    List<ShowVote> showVoteList = showVoteDao.findByPersonId(showVoteVO.getPersonId());
    Integer personVoteNum = Tools
        .getInt(redisUtil.getString(RedisKey.PERSON_VOTE_NUM), commConfig.getPersonVoteNum());
    // 判断有无剩余投票次数
    if (showVoteList.size() == personVoteNum) {
      return ResultUtil.error(ErrCode.NO_VOTE_NUM);
    }

    // 判断是否重复投票
    if (!showVoteList.isEmpty()) {
      boolean anyMatch = showVoteList.stream()
          .anyMatch(showVote -> showVote.getShowId().equals(showVoteVO.getShowId()));
      if (anyMatch) {
        return ResultUtil.error(ErrCode.HAD_VOTE);
      }
    }

    ShowVote showVote = new ShowVote();
    BeanUtils.copyProperties(showVoteVO, showVote);
    showVote.fillDefaultProperty();
    showVoteDao.insert(showVote);
    return ResultUtil.success();
  }

  @Override
  public RespMsg setState(ShowVoteStateVO showVoteStateVO) {
    Date startTime = showVoteStateVO.getStartTime();
    if (startTime != null) {
      redisUtil.set(RedisKey.VOTE_START_TIME, startTime);
      quartzManager
          .saveJob(QuartzJobName.VOTE, ShowVoteJob.class, commConfig.getVoteTaskInterval());
    }
    Date endTime = showVoteStateVO.getEndTime();
    if (endTime != null) {
      redisUtil.set(RedisKey.VOTE_END_TIME, endTime);
      quartzManager.saveJob(QuartzJobName.VOTE, ShowVoteJob.class, commConfig.getVoteTaskInterval(),
          endTime);
    }
    Integer personVoteNum = showVoteStateVO.getPersonVoteNum();
    if (personVoteNum != null) {
      redisUtil.set(RedisKey.PERSON_VOTE_NUM, personVoteNum.toString());
    }
    return ResultUtil.success();
  }

}
