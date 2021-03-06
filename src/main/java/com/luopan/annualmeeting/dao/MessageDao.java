package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.Message;
import com.luopan.annualmeeting.entity.vo.LimitVO;
import com.luopan.annualmeeting.entity.vo.MessageGetVO;
import com.luopan.annualmeeting.entity.vo.MessageManageVO;
import com.luopan.annualmeeting.entity.vo.MessageSearchVO;
import com.luopan.annualmeeting.entity.vo.MessageVO;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageDao {

  int insert(Message message);

  long countAll();

  List<MessageManageVO> search(MessageSearchVO messageSearchVO);

  Message findById(long id);

  int updateSelective(Message message);

  List<MessageVO> findSendMessages(LimitVO limitVO);

  List<Message> findSelfMessages(long personId);

  List<MessageVO> findRefreshMessages(MessageGetVO messageGetVO);

  List<MessageVO> findLoadMessages(MessageGetVO messageGetVO);

}
