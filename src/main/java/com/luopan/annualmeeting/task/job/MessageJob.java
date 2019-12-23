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
import java.util.Optional;
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
//    log.info("【==>  消息上墙定时任务推送】");
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    String[] array = jobName.split(Constant.SPLITTER_COLON);
    Long companyId = Tools.getLong(array[1]);

    String keyNoNewMessageOffset =
        RedisKey.NO_NEW_MESSAGE_OFFSET + Constant.SPLITTER_COLON + companyId;
    String keyMessageOffset = RedisKey.MESSAGE_OFFSET + Constant.SPLITTER_COLON + companyId;

    boolean noNewMessages = false;
    int num = Optional.ofNullable(redisUtil
        .get(RedisKey.MESSAGE_TASK_NUM + Constant.SPLITTER_COLON + companyId, Integer.class))
        .orElse(Constant.MESSAGE_TASK_NUM);
    long offset = Optional.ofNullable(redisUtil.get(keyMessageOffset, Integer.class))
        .orElse(Constant.MESSAGE_DEFAULT_OFFSET);
    List<MessageVO> list = messageService.findSendMessages(offset, num, companyId);
    if (list == null || list.isEmpty()) {
      offset = Tools
          .getLong(redisUtil.getString(keyNoNewMessageOffset), Constant.MESSAGE_DEFAULT_OFFSET);
      list = messageService.findSendMessages(offset, num, companyId);
      if (list.isEmpty() && Constant.MESSAGE_DEFAULT_OFFSET != offset) {
        offset = Constant.MESSAGE_DEFAULT_OFFSET;
        list = messageService.findSendMessages(offset, num, companyId);
      }
      noNewMessages = true;
    }
    if (!list.isEmpty()) {
      WebSocketMessageVO<List<MessageVO>> vo = new WebSocketMessageVO<>(
          WebSocketMessageType.MESSAGE, list);
      serverManageWebSocket.sendMessageAll(JsonUtil.obj2String(vo), companyId);
      redisUtil.set(noNewMessages ? keyNoNewMessageOffset : keyMessageOffset, offset + list.size());
    }
  }

}
