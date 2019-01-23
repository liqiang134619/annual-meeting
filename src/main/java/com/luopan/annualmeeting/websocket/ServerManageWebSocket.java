package com.luopan.annualmeeting.websocket;

import com.luopan.annualmeeting.common.Constant;
import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import com.luopan.annualmeeting.entity.vo.ShowVoteCountVO;
import com.luopan.annualmeeting.entity.vo.SignInPersonVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.service.IPersonService;
import com.luopan.annualmeeting.service.IShowService;
import com.luopan.annualmeeting.util.BeanUtil;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.util.RedisUtil;
import com.luopan.annualmeeting.util.Tools;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@ServerEndpoint(value = "/serverManage/{companyId}")
public class ServerManageWebSocket {

  private Session session;

  private Long companyId;

  // 纪录当前连接数
  private static AtomicInteger count = new AtomicInteger();

  // 存放已连接的客户端
  private static ConcurrentHashMap<Long, Set<ServerManageWebSocket>> websocketMap = new ConcurrentHashMap<>();

  private static IPersonService personService;

  private static IShowService showService;

  private static IMessageService messageService;

  private static RedisUtil redisUtil;

  @OnOpen
  public void onOpen(@PathParam("companyId") Long companyId, Session session) {
    log.info("后台管理当前连接数：" + count.incrementAndGet());
    this.companyId = companyId;
    this.session = session;

    websocketMap
        .computeIfAbsent(companyId, compId -> new CopyOnWriteArraySet<>()).add(this);

    // 已签到人员
    List<SignInPersonVO> signInPersonList = personService.findSignInPersonList(companyId);
    if (BeanUtil.isNotEmpty(signInPersonList)) {
      WebSocketMessageVO<List<SignInPersonVO>> webSocketMessageVO = new WebSocketMessageVO<>(
          WebSocketMessageType.SIGN_IN, signInPersonList);
      sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), session);
    }
    // 节目投票
    List<ShowVoteCountVO> showVoteCountVOList = showService.findShowVoteCountVOList(companyId);
    if (BeanUtil.isNotEmpty(showVoteCountVOList)) {
      WebSocketMessageVO<List<ShowVoteCountVO>> webSocketMessageVO = new WebSocketMessageVO<>(
          WebSocketMessageType.SHOW_VOTE, showVoteCountVOList);
      sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), session);
    }
    // 留言墙
    List<MessageVO> firstSendMessageList = getFirstSendMessages();
    if (BeanUtil.isNotEmpty(firstSendMessageList)) {
      WebSocketMessageVO<List<MessageVO>> webSocketMessageVO = new WebSocketMessageVO<>(
          WebSocketMessageType.MESSAGE, firstSendMessageList);
      sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), session);
    }
  }

  private List<MessageVO> getFirstSendMessages() {
    int num = Tools
        .getInt(
            redisUtil.getString(RedisKey.MESSAGE_TASK_NUM + Constant.SPLITTER_COLON + companyId),
            Constant.MESSAGE_TASK_NUM);
    long offset = Tools
        .getLong(redisUtil.getString(RedisKey.MESSAGE_OFFSET + Constant.SPLITTER_COLON + companyId),
            Constant.MESSAGE_DEFAULT_OFFSET);
    offset -= num;
    if (offset < Constant.MESSAGE_DEFAULT_OFFSET) {
      offset = Constant.MESSAGE_DEFAULT_OFFSET;
    }
    List<MessageVO> list = messageService.findSendMessages(offset, num, companyId);
    return list;
  }

  @OnClose
  public void onClose() {
    log.info("后台管理当前连接数：" + count.decrementAndGet());
    websocketMap.get(companyId).remove(this);
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("后台管理WebSocket服务端发生了错误", error);
  }

  @OnMessage
  public void onMessage(String message) {

  }

  public void sendMessageAll(String message, Long companyId) {
    if (!StringUtils.isEmpty(message) && companyId != null) {
      Set<ServerManageWebSocket> websocketSet = websocketMap.get(companyId);
      if (BeanUtil.isNotEmpty(websocketSet)) {
        websocketSet.forEach(webSocket -> {
          Session s = webSocket.session;
          synchronized (s) {
            try {
              s.getBasicRemote().sendText(message);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
      }
    }
  }

  public void sendMessageTo(String message, Session session) {
    if (!StringUtils.isEmpty(message) && session != null) {
      try {
        synchronized (session) {
          session.getBasicRemote().sendText(message);
        }
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
  }

}
