package com.luopan.annualmeeting.websocket;

import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.entity.vo.SelfMessageVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.util.JsonUtil;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
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
@ServerEndpoint(value = "/websocket/{personId}")
public class WebSocket {

  private Session session;

  private Long personId;

  // 记录当前连接数
  private static AtomicLong count = new AtomicLong();

  // 存放客户端对应Session
  private static ConcurrentHashMap<Long, WebSocket> webSocketMap = new ConcurrentHashMap<>();

  private static IMessageService messageService;

  @OnOpen
  public void onOpen(@PathParam("personId") Long personId, Session session) {
    this.session = session;
    this.personId = personId;
    webSocketMap.put(personId, this);
    log.info("当前连接数：{}", count.incrementAndGet());
    // 连接后发送本人已发消息
    List<SelfMessageVO> selfMessages = messageService.findSelfMessages(personId);
    if (selfMessages != null && !selfMessages.isEmpty()) {
      WebSocketMessageVO<List<SelfMessageVO>> webSocketMessageVO = new WebSocketMessageVO<>(
          WebSocketMessageType.MESSAGE, selfMessages);
      sendMessageTo(JsonUtil.obj2String(webSocketMessageVO), session);
    }
  }

  @OnClose
  public void onClose() {
    webSocketMap.remove(this.personId);
    log.info("当前连接数：{}", count.decrementAndGet());
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("WebSocket服务端发生了错误", error);
  }

  @OnMessage
  public void onMessage(String message) {
    // 发送消息
    messageService.sendMessage(personId, message);
  }

  public void sendMessageAll(String message) {
    if (!StringUtils.isEmpty(message)) {
      webSocketMap.values().forEach(webSocket -> {
        try {
          webSocket.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    }
  }

  public void sendMessageTo(String message, Long targetPersonId) {
    if (targetPersonId != null && !StringUtils.isEmpty(message)) {
      WebSocket webSocket = webSocketMap.get(targetPersonId);
      if (webSocket != null) {
        try {
          webSocket.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void sendMessageTo(String message, Session session) {
    try {
      session.getBasicRemote().sendText(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void fillAutowireProperty(ApplicationContext applicationContext) {
    messageService = (IMessageService) applicationContext.getBean("messageService");
  }

}
