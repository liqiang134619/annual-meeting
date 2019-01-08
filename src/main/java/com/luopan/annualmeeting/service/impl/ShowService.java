package com.luopan.annualmeeting.service.impl;

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
import com.luopan.annualmeeting.service.IShowService;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public RespMsg findAll() {
    List<ShowVO> showVOList = new LinkedList<>();
    List<Show> list = showDao.findAll();
    if (list != null && !list.isEmpty()) {
      list.forEach(show -> {
        ShowVO showVO = new ShowVO();
        BeanUtils.copyProperties(show, showVO);
        showVOList.add(showVO);
      });
    }
    return ResultUtil.success(showVOList);
  }

  @Transactional
  @Override
  public RespMsg insert(ShowVO showVO) {
    Show show = new Show();
    BeanUtils.copyProperties(showVO, show);
    show.fillDefaultProperty();
    showDao.insert(show);
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
  public List<ShowVoteCountVO> findShowVoteCountVOList() {
    return showDao.findShowVoteCountVOList();
  }
}
