package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.Status;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.PraiseDao;
import com.luopan.annualmeeting.dao.ShowDao;
import com.luopan.annualmeeting.dao.ShowScoreDao;
import com.luopan.annualmeeting.entity.Praise;
import com.luopan.annualmeeting.entity.Show;
import com.luopan.annualmeeting.entity.ShowScore;
import com.luopan.annualmeeting.entity.vo.PraiseVO;
import com.luopan.annualmeeting.entity.vo.ShowScorePraiseCommentVO;
import com.luopan.annualmeeting.entity.vo.ShowScoreVO;
import com.luopan.annualmeeting.entity.vo.ShowVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteCountVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteNowVO;
import com.luopan.annualmeeting.service.IShowService;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ShowService implements IShowService {

  @Autowired
  private ShowDao showDao;

  @Autowired
  private PraiseDao praiseDao;

  @Autowired
  private ShowScoreDao showScoreDao;

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public RespMsg findAll(Long companyId) {
    List<Show> list = showDao.findAll(companyId);
    List<ShowVO> showVOList = Optional.ofNullable(BeanUtil.copyProperties(list, ShowVO.class))
        .orElse(Collections.emptyList());
    Set<Long> showIds = Optional.ofNullable(redisUtil
        .sGet(RedisKey.VOTEABLE_SHOW + Constant.SPLITTER_COLON + companyId, Long.class))
        .orElse(Collections.emptySet());
    showVOList.forEach(showVO -> showVO
        .setVoteNow(showIds.contains(showVO.getId()) ? Status.ENABLE : Status.DISABLE));
    return ResultUtil.success(showVOList);
  }

  @Transactional
  @Override
  public RespMsg insert(ShowVO showVO) {
    if (StringUtils.isEmpty(showVO.getName()) || showVO.getVote() == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }
    Show show = BeanUtil.copyProperties(showVO, Show.class);
    show.fillDefaultProperty();
    showDao.insert(show);
    return ResultUtil.success();
  }

  @Override
  public RespMsg voteNow(ShowVoteNowVO showVoteNowVO) {
    Long showId = showVoteNowVO.getShowId();
    Integer voteAble = showVoteNowVO.getVoteAble();
    if (showId == null || voteAble == null) {
      return ResultUtil.error(ErrCode.ILLEGAL_ARGUMENT);
    }
    Show show = showDao.findById(showId);
    if (show == null) {
      return ResultUtil.error(ErrCode.SHOW_NOT_FOUND);
    }
    if (show.getVote() == Status.DISABLE) {
      return ResultUtil.error(ErrCode.SHOW_NOT_JOIN_VOTE);
    }
    if (voteAble == Status.ENABLE) {
      redisUtil
          .sSet(RedisKey.VOTEABLE_SHOW + Constant.SPLITTER_COLON + showVoteNowVO.getCompanyId(),
              showId);
    } else {
      redisUtil.sRemove(
          RedisKey.VOTEABLE_SHOW + Constant.SPLITTER_COLON + showVoteNowVO.getCompanyId(), showId);
    }
    return ResultUtil.success();
  }

  @Transactional
  @Override
  public RespMsg praise(PraiseVO praiseVO) {
    int count = praiseDao.count(praiseVO.getPersonId(), praiseVO.getShowId());
    if (count > 0) {
      return ResultUtil.error(ErrCode.HAD_PRAISE);
    }
    Praise praise = new Praise();
    BeanUtils.copyProperties(praiseVO, praise);
    praise.fillDefaultProperty();
    praiseDao.insert(praise);
    return ResultUtil.success();
  }

  @Transactional
  @Override
  public RespMsg score(ShowScoreVO showScoreVO) {
    int count = showScoreDao.count(showScoreVO.getPersonId(), showScoreVO.getShowId());
    if (count > 0) {
      return ResultUtil.error(ErrCode.HAD_SCORE);
    }
    ShowScore showScore = new ShowScore();
    BeanUtils.copyProperties(showScoreVO, showScore);
    showScore.fillDefaultProperty();
    showScoreDao.insert(showScore);
    return ResultUtil.success();
  }

  @Override
  public RespMsg findShowScorePraiseCommentVOList(long personId) {
    List<ShowScorePraiseCommentVO> list = showDao.findShowScorePraiseCommentVOList();
    if (list != null && !list.isEmpty()) {
      // 已点赞节目
      final Set<Long> hadPraiseSet;
      List<Praise> praiseList = praiseDao.findByPersonId(personId);
      if (praiseList != null && !praiseList.isEmpty()) {
        hadPraiseSet = praiseList.stream().map(Praise::getShowId).collect(Collectors.toSet());
      } else {
        hadPraiseSet = new HashSet<>();
      }
      // 已评分节目
      final Map<Long, Integer> hadScoreMap;
      List<ShowScore> scoreList = showScoreDao.findByPersonId(personId);
      if (scoreList != null && !scoreList.isEmpty()) {
        hadScoreMap = scoreList.stream()
            .collect(Collectors.toMap(ShowScore::getShowId, ShowScore::getScore));
      } else {
        hadScoreMap = new HashMap<>();
      }
      list.forEach(vo -> {
        Long id = vo.getId();
        vo.setIsPraise(hadPraiseSet.contains(id) ? Status.ENABLE : Status.DISABLE);
        Integer score = hadScoreMap.get(id);
        if (score == null) {
          vo.setIsScore(Status.DISABLE);
        } else {
          vo.setIsScore(Status.ENABLE);
          vo.setScore(score);
        }
      });
    }

    return ResultUtil.success(list);
  }

  @Override
  public List<ShowVoteCountVO> findShowVoteCountVOList(Long companyId) {
    List<Show> shows = Optional.ofNullable(showDao.findVoteAbleList(companyId))
        .orElse(Collections.emptyList());
    Map<Long, Long> voteMap = Optional.ofNullable(redisUtil
        .hGetAll(RedisKey.SHOW_VOTE_NUM + Constant.SPLITTER_COLON + companyId, Long.class,
            Long.class)).orElse(new HashMap<>());
    List<ShowVoteCountVO> list = shows.stream().map(show -> {
      ShowVoteCountVO showVoteCountVO = BeanUtil.copyProperties(show, ShowVoteCountVO.class);
      showVoteCountVO
          .setVoteCount(Optional.ofNullable(voteMap.get(show.getId())).orElse(0L).intValue());
      return showVoteCountVO;
    }).collect(Collectors.toList());

    return list;
  }

  @Transactional
  @Override
  public RespMsg delete(Long id) {
    Show show = new Show();
    show.setId(id).setStatus(Status.DISABLE).setUpdateTime(new Date());
    int rows = showDao.updateByPrimaryKeySelective(show);
    if (rows == 0) {
      return ResultUtil.error(ErrCode.SHOW_DELETE_ERROR);
    }
    return ResultUtil.success();
  }
}
