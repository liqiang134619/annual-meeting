package com.luopan.annualmeeting.service;

import com.luopan.annualmeeting.common.RespMsg;
import com.luopan.annualmeeting.entity.Message;
import com.luopan.annualmeeting.entity.vo.MessageCheckVO;
import com.luopan.annualmeeting.entity.vo.MessageSearchVO;
import com.luopan.annualmeeting.entity.vo.MessageTopVO;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import java.util.List;

public interface IMessageService {

  int insert(Message message);

  List<MessageVO> findSendMessages(List<Long> ids);

  RespMsg search(MessageSearchVO messageSearchVO);

  RespMsg check(MessageCheckVO messageCheckVO);

  RespMsg top(MessageTopVO messageTopVO);

}
