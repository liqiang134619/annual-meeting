package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.ShowVoteDao;
import com.luopan.annualmeeting.entity.ShowVote;
import com.luopan.annualmeeting.entity.vo.ShowVoteRemainVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteVO;
import com.luopan.annualmeeting.service.IShowVoteService;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShowVoteService implements IShowVoteService {

  @Autowired
  private ShowVoteDao showVoteDao;

  @Autowired
  private RedisUtil redisUtil;

  @Transactional
  @Override
  public RespMsg vote(ShowVoteVO showVoteVO) {
    Long personId = showVoteVO.getPersonId();
    Long showId = showVoteVO.getShowId();
    Long companyId = showVoteVO.getCompanyId();

    if (personId == null || showId == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }

    // 判断节目是否开启投票
    boolean b = redisUtil
        .sContain(RedisKey.VOTEABLE_SHOW + Constant.SPLITTER_COLON + companyId, showId);
    if (!b) {
      return ResultUtil.error(ErrCode.SHOW_VOTE_NOT_START);
    }

    // 判断剩余票数
    int remainVoteNum = getRemainVoteNum(personId, companyId);
    if (remainVoteNum == 0) {
      return ResultUtil.error(ErrCode.NO_VOTE_COUNT);
    }

    // 判断重复投票
    int count = showVoteDao.countByPersonIdAndShowId(personId, showId);
    if (count > 0) {
      return ResultUtil.error(ErrCode.HAD_VOTE);
    }

    ShowVote showVote = BeanUtil.copyProperties(showVoteVO, ShowVote.class);
    showVote.fillDefaultProperty();
    showVoteDao.insert(showVote);

    // 节目投票数+1
    redisUtil.hIncr(RedisKey.SHOW_VOTE_NUM + Constant.SPLITTER_COLON + companyId, showId);
    return ResultUtil.success();
  }

  @Override
  public RespMsg my(Long personId) {
    if (personId == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }
    List<ShowVote> myVoteList = showVoteDao.findByPersonId(personId);
    return ResultUtil.success(myVoteList);
  }

  @Override
  public RespMsg remain(Long personId, Long companyId) {
    if (personId == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }
    ShowVoteRemainVO showVoteRemainVO = new ShowVoteRemainVO()
        .setRemain(getRemainVoteNum(personId, companyId));
    return ResultUtil.success(showVoteRemainVO);
  }

  private int getRemainVoteNum(Long personId, Long companyId) {
    int count = showVoteDao.countByPersonId(personId);
    Integer showVoteNum = Optional
        .ofNullable(redisUtil
            .get(RedisKey.PERSON_VOTE_NUM + Constant.SPLITTER_COLON + companyId, Integer.class))
        .orElse(Constant.PERSON_VOTE_NUM);
    return showVoteNum - count;
  }

}
