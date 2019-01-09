package com.luopan.annualmeeting.websocket;

import com.luopan.annualmeeting.common.Constant.RedisKey;
import com.luopan.annualmeeting.entity.Message;
import com.luopan.annualmeeting.service.IMessageService;
import com.luopan.annualmeeting.util.RedisUtil;
import java.io.IOException;
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

  private static RedisUtil redisUtil;

  @OnOpen
  public void onOpen(@PathParam("personId") Long personId, Session session) {
    this.session = session;
    this.personId = personId;
    webSocketMap.put(personId, this);
    log.info("当前连接数：{}", count.incrementAndGet());
  }

  @OnClose
  public void onClose() {
    webSocketMap.remove(this.personId);
    log.info("当前连接数：{}", count.decrementAndGet());
  }

  @OnError
  public void onError(Session session, Throwable error) {
    log.error("WebSocket服务端发生了错误" + error.getMessage());
  }

  @OnMessage
  public void onMessage(String message) {
    if (redisUtil.sContain(RedisKey.BANNED_PERSON_ID, personId.toString())) {
      return;
    }
    Message msg = new Message();
    msg.setPersonId(personId);
    msg.setMessage(message);
    msg.fillDefaultProperty();
    messageService.insert(msg);
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

  public static void fillAutowireProperty(ApplicationContext applicationContext) {
    messageService = (IMessageService) applicationContext.getBean("messageService");
    redisUtil = (RedisUtil) applicationContext.getBean("redisUtil");
  }

}
