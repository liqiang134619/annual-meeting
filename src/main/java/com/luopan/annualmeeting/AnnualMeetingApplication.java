package com.luopan.annualmeeting;

import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AnnualMeetingApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication
        .run(AnnualMeetingApplication.class, args);
    // 解决websocket属性无法注入的问题
    ServerManageWebSocket.fillAutowireProperty(applicationContext);
  }

}

