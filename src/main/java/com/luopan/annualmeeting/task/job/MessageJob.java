package com.luopan.annualmeeting.task.job;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.config.CommConfig;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.Tools;
import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MessageJob implements Job {

  @Autowired
  private IMessageService messageService;

  @Autowired
  private ServerManageWebSocket serverManageWebSocket;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private CommConfig commConfig;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) {
    boolean noNewMessages = false;
    int num = Tools
        .getInt(redisUtil.getString(RedisKey.MESSAGE_TASK_NUM), commConfig.getMessageTaskNum());
    long index = Tools
        .getLong(redisUtil.getString(RedisKey.MESSAGE_LAST_INDEX), Constant.MESSAGE_DEFAULT_INDEX);
    List<MessageVO> list = getSendMessages(index, num);
    if (list.isEmpty()) {
      index = Tools
          .getLong(redisUtil.getString(RedisKey.NO_NEW_MESSAGE_LAST_INDEX),
              Constant.MESSAGE_DEFAULT_INDEX);
      list = getSendMessages(index, num);
      if (list.isEmpty() && Constant.MESSAGE_DEFAULT_INDEX != index) {
        index = Constant.MESSAGE_DEFAULT_INDEX;
        list = getSendMessages(index, num);
      }
      noNewMessages = true;
    }
    if (!list.isEmpty()) {
      WebSocketMessageVO<List<MessageVO>> vo = new WebSocketMessageVO<>();
      vo.setType(WebSocketMessageType.MESSAGE);
      vo.setData(list);
      serverManageWebSocket.sendMessageAll(JsonUtil.obj2String(vo));
      redisUtil
          .setString(
              noNewMessages ? RedisKey.NO_NEW_MESSAGE_LAST_INDEX : RedisKey.MESSAGE_LAST_INDEX,
              index + list.size() + "");
    }
  }

  private List<MessageVO> getSendMessages(long index, int num) {
    List<MessageVO> list = new LinkedList<>();
    long startId = index;
    long endId = startId + num - 1;
    List<Object> objects = redisUtil.lGet(RedisKey.CHECK_PASS_MESSAGES, startId, endId);
    if (objects != null && !objects.isEmpty()) {
      List<Long> ids = objects.stream().map(object -> Long.parseLong(object.toString()))
          .collect(Collectors.toList());
      list = messageService.findSendMessages(ids);
    }
    return list;
  }

}
