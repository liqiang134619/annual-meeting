package com.luopan.annualmeeting.service.impl;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RewardLevel;
import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.common.ErrCode;
import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.dao.LotteryDao;
import com.luopan.annualmeeting.dao.PersonDao;
import com.luopan.annualmeeting.dao.RewardDao;
import com.luopan.annualmeeting.entity.Lottery;
import com.luopan.annualmeeting.entity.Person;
import com.luopan.annualmeeting.entity.Reward;
import com.luopan.annualmeeting.entity.vo.LotteryPersonRewardVO;
import com.luopan.annualmeeting.entity.vo.LotteryPersonVO;
import com.luopan.annualmeeting.entity.vo.LotteryRewardVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.ILotteryService;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.ResultUtil;
import com.luopan.annualmeeting.websocket.WebSocket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LotteryService implements ILotteryService {

  @Autowired
  private RewardDao rewardDao;

  @Autowired
  private PersonDao personDao;

  @Autowired
  private LotteryDao lotteryDao;

  @Autowired
  private WebSocket webSocket;

  @Transactional
  @Override
  public RespMsg lottery(long rewardId) {
    Reward reward = rewardDao.findById(rewardId);
    if (reward == null) {
      return ResultUtil.error(ErrCode.NO_REWARD);
    }
    List<Person> personList = personDao.findNoLotteryPeople();
    if (personList == null || personList.isEmpty()) {
      return ResultUtil.error(ErrCode.NO_PERSON);
    }

    // 特等奖从留言和评分人员中抽
    if (reward.getLevel() == RewardLevel.GRAND) {
      Set<Long> ids = personDao.findJoinGrandPrizePersonIds();
      final Set<Long> grandIds = ids != null ? ids : new HashSet<>();
      personList = personList.stream().filter(person -> grandIds.contains(person.getId()))
          .collect(Collectors.toList());
    }
    /*List<Lottery> lotteryList = lotteryDao.findAll();
    // 排除已中奖人员
    if (lotteryList != null && !lotteryList.isEmpty()) {
      Set<Long> lotteryPersonIds = lotteryList.stream().map(Lottery::getPersonId)
          .collect(Collectors.toSet());
      personList = personList.stream()
          .filter(person -> !lotteryPersonIds.contains(person.getId())).collect(
              Collectors.toList());
    }*/

    // 参与抽奖人数
    int personNum = personList.size();
    // 奖品数量
    int rewardNum = reward.getAmount();
    // 判断参与抽奖人数是否足够
    if (personNum < rewardNum) {
      return ResultUtil.error(ErrCode.NO_ENOUGH_PERSON);
    }

    // 抽取中奖人
    int[] source = new int[personNum];
    for (int i = 0; i < personNum; i++) {
      source[i] = i;
    }
    int[] result = new int[rewardNum];
    Random random = new Random();
    for (int i = 0; i < rewardNum; i++) {
      // 从参与抽奖人数中随机一个下标
      int index = random.nextInt(personNum--);
      // 将随机到的数放入结果集
      result[i] = source[index];
      // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
      source[index] = source[personNum];
    }

    // 本轮中奖人员
    List<LotteryPersonVO> lotteryPersonVOList = new LinkedList<>();
    for (int i = 0, len = result.length; i < len; i++) {
      // 将中奖人放入结果集
      LotteryPersonVO vo = new LotteryPersonVO();
      Person person = personList.get(result[i]);
      BeanUtils.copyProperties(person, vo);
      lotteryPersonVOList.add(vo);

      // 将中奖纪录入库
      Lottery lottery = new Lottery();
      lottery.setPersonId(person.getId());
      lottery.setRewardId(reward.getId());
      lottery.fillDefaultProperty();
      lotteryDao.insert(lottery);

      // todo 推送中奖通知 这边可以集成消息队列
      WebSocketMessageVO<String> webSocketMessageVO = new WebSocketMessageVO<>();
      webSocketMessageVO.setType(WebSocketMessageType.LOTTERY);
      webSocketMessageVO.setData(Constant.LOTTERY_MESSAGE);
      webSocket.sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), person.getId());
    }

    return ResultUtil.success(lotteryPersonVOList);
  }

  @Override
  public RespMsg findAllLotteryPeople() {
    List<LotteryPersonRewardVO> list = lotteryDao.findLotteryPeople();
    return ResultUtil.success(list);
  }

  @Override
  public RespMsg findRewardLotteryPeople(long rewardId) {
    List<LotteryPersonVO> list = lotteryDao
        .findLotteryPeopleByRewardId(rewardId);
    return ResultUtil.success(list);
  }

  @Override
  public RespMsg findRewardByPersonId(long personId) {
    List<LotteryRewardVO> list = lotteryDao.findRewardByPersonId(personId);
    return ResultUtil.success(list);
  }

}
