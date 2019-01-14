package com.luopan.annualmeeting.task.job;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.Tools;
import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import java.util.List;
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

  @Override
  public void execute(JobExecutionContext jobExecutionContext) {
    boolean noNewMessages = false;
    int num = Tools
        .getInt(redisUtil.getString(RedisKey.MESSAGE_TASK_NUM), Constant.MESSAGE_TASK_NUM);
    long offset = Tools
        .getLong(redisUtil.getString(RedisKey.MESSAGE_OFFSET), Constant.MESSAGE_DEFAULT_OFFSET);
    List<MessageVO> list = messageService.findSendMessages(offset, num);
    if (list == null || list.isEmpty()) {
      offset = Tools
          .getLong(redisUtil.getString(RedisKey.NO_NEW_MESSAGE_OFFSET),
              Constant.MESSAGE_DEFAULT_OFFSET);
      list = messageService.findSendMessages(offset, num);
      if (list.isEmpty() && Constant.MESSAGE_DEFAULT_OFFSET != offset) {
        offset = Constant.MESSAGE_DEFAULT_OFFSET;
        list = messageService.findSendMessages(offset, num);
      }
      noNewMessages = true;
    }
    if (!list.isEmpty()) {
      WebSocketMessageVO<List<MessageVO>> vo = new WebSocketMessageVO<>(
          WebSocketMessageType.MESSAGE, list);
      serverManageWebSocket.sendMessageAll(JsonUtil.obj2String(vo));
      redisUtil
          .setString(
              noNewMessages ? RedisKey.NO_NEW_MESSAGE_OFFSET : RedisKey.MESSAGE_OFFSET,
              offset + list.size() + "");
    }
  }

}
