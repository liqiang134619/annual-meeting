package com.luopan.annualmeeting.task.job;

import com.luopan.annualmeeting.common.Constant.WebSocketMessageType;
import com.luopan.annualmeeting.entity.vo.ShowVoteCountVO;
import com.luopan.annualmeeting.entity.vo.WebSocketMessageVO;
import com.luopan.annualmeeting.service.IShowService;
import com.luopan.annualmeeting.util.JsonUtil;
import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ShowVoteJob implements Job {

  @Autowired
  private IShowService showService;

  @Autowired
  private ServerManageWebSocket serverManageWebSocket;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    List<ShowVoteCountVO> showVoteCountVOList = showService.findShowVoteCountVOList();
    if (showVoteCountVOList != null && !showVoteCountVOList.isEmpty()) {
      WebSocketMessageVO<List<ShowVoteCountVO>> webSocketMessageVO = new WebSocketMessageVO<>(
          WebSocketMessageType.SHOW_VOTE, showVoteCountVOList);
      serverManageWebSocket.sendMessageAll(JsonUtil.obj2String(webSocketMessageVO));
    }
  }

}
