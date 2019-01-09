package com.luopan.annualmeeting.websocket;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.config.CommConfig;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteCountVO;
import com.luopan.annualmeeting.entity.vo.SignInPersonVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.service.IPersonService;
import com.luopan.annualmeeting.service.IShowService;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.Tools;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@ServerEndpoint(value = "/serverManage")
public class ServerManageWebSocket {

  private Session session;

  // 纪录当前连接数
  private static AtomicInteger count = new AtomicInteger();

  // 存放已连接的客户端
  private static CopyOnWriteArraySet<ServerManageWebSocket> websocketSet = new CopyOnWriteArraySet<>();

  private static IPersonService personService;

  private static IShowService showService;

  private static IMessageService messageService;

  private static RedisUtil redisUtil;

  private static CommConfig commConfig;

  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    websocketSet.add(this);
    // 已签到人员
    List<SignInPersonVO> signInPersonList = personService.findSignInPersonList();
    if (signInPersonList != null && !signInPersonList.isEmpty()) {
      WebSocketMessageVO<List<SignInPersonVO>> webSocketMessageVO = new WebSocketMessageVO<>();
      webSocketMessageVO.setType(WebSocketMessageType.SIGN_IN);
      webSocketMessageVO.setData(signInPersonList);
      sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), session);
    }
    // 节目投票
    List<ShowVoteCountVO> showVoteCountVOList = showService.findShowVoteCountVOList();
    if (showVoteCountVOList != null && !showVoteCountVOList.isEmpty()) {
      WebSocketMessageVO<List<ShowVoteCountVO>> webSocketMessageVO = new WebSocketMessageVO<>();
      webSocketMessageVO.setType(WebSocketMessageType.SHOW_VOTE);
      webSocketMessageVO.setData(showVoteCountVOList);
      sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), session);
    }
    // 留言墙
    List<MessageVO> firstSendMessageList = getFirstSendMessages();
    if (firstSendMessageList != null && !firstSendMessageList.isEmpty()) {
      WebSocketMessageVO<List<MessageVO>> webSocketMessageVO = new WebSocketMessageVO<>();
      webSocketMessageVO.setType(WebSocketMessageType.MESSAGE);
      webSocketMessageVO.setData(firstSendMessageList);
      sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), session);
    }

    log.info("后台管理当前连接数：" + count.incrementAndGet());
  }

  private List<MessageVO> getFirstSendMessages() {
    List<MessageVO> list = new LinkedList<>();
    int num = Tools
        .getInt(redisUtil.getString(RedisKey.MESSAGE_TASK_NUM), commConfig.getMessageTaskNum());
    long index = Tools
        .getLong(redisUtil.getString(RedisKey.MESSAGE_LAST_INDEX), Constant.MESSAGE_DEFAULT_INDEX);
    long startId = index - num;
    if (startId < Constant.MESSAGE_DEFAULT_INDEX) {
      startId = Constant.MESSAGE_DEFAULT_INDEX;
    }
    long endId = index - 1;
    if (endId < Constant.MESSAGE_DEFAULT_INDEX) {
      endId = Constant.MESSAGE_DEFAULT_INDEX;
    }
    List<Object> objects = redisUtil.lGet(RedisKey.CHECK_PASS_MESSAGES, startId, endId);
    if (objects != null && !objects.isEmpty()) {
      List<Long> ids = objects.stream().map(object -> Long.parseLong(object.toString()))
          .collect(Collectors.toList());
      list = messageService.findSendMessages(ids);
    }
    return list;
  }

  @OnClose
  public void onClose() {
    websocketSet.remove(this);
    log.info("后台管理当前连接数：" + count.decrementAndGet());
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("后台管理WebSocket服务端发生了错误" + error.getMessage());
  }

  @OnMessage
  public void onMessage(String message) {

  }

  public void sendMessageAll(String message) {
    if (!StringUtils.isEmpty(message)) {
      websocketSet.forEach(webSocket -> {
        try {
          webSocket.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
  }

  public void sendMessageTo(String message, Session session) {
    if (!StringUtils.isEmpty(message) && session != null) {
      try {
        session.getBasicRemote().sendText(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void fillAutowireProperty(ApplicationContext applicationContext) {
    personService = (IPersonService) applicationContext.getBean("personService");
    showService = (IShowService) applicationContext.getBean("showService");
    messageService = (IMessageService) applicationContext.getBean("messageService");
    redisUtil = (RedisUtil) applicationContext.getBean("redisUtil");
    commConfig = (CommConfig) applicationContext.getBean("commConfig");
  }

}
