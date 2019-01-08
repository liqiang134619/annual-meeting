package com.luopan.annualmeeting.common;

public class Constant {

  public interface Status {

    int ENABLE = 1;
    int DISABLE = 0;
  }

  /**
   * websocket消息类型
   */
  public interface WebSocketMessageType {

    // 中奖
    int LOTTERY = 1;
    // 留言
    int MESSAGE = 2;
    // 节目投票
    int SHOW_VOTE = 3;
    // 签到人员
    int SIGN_IN = 4;
  }

  public interface RewardLevel {

    // 特等奖
    int GRAND = 0;
  }

  public interface RedisKey {

    // 任务间隔
    String MESSAGE_TASK_INTERVAL = "MESSAGE_TASK_INTERVAL";

    // 任务消息数
    String MESSAGE_TASK_NUM = "MESSAGE_TASK_NUM";

    // 上次读取消息的序号
    String MESSAGE_LAST_INDEX = "MESSAGE_LAST_INDEX";

    // 没有新消息时的上次读取消息的序号
    String NO_NEW_MESSAGE_LAST_INDEX = "NO_NEW_MESSAGE_LAST_INDEX";

    // 审核通过留言
    String CHECK_PASS_MESSAGES = "CHECK_PASS_MESSAGES";

    // 已被禁言人员ID
    String BANNED_PERSON_ID = "BANNED_PERSON_ID";

    // 投票开始时间
    String VOTE_START_TIME = "VOTE_START_TIME";

    // 投票结束时间
    String VOTE_END_TIME = "VOTE_END_TIME";

    // 单人投票次数
    String PERSON_VOTE_NUM = "PERSON_VOTE_NUM";
  }

  /**
   * 定时任务名
   */
  public interface QuartzJobName {

    String MESSAGE = "MESSAGE";

    String VOTE = "VOTE";
  }

  /**
   * 用户角色
   */
  public interface Role {

    // 管理员
    String ADMIN = "ADMIN";

    // 角色
    String USER = "USER";
  }

  /**
   * 签到类型
   */
  public interface SignType {

    // 扫码
    int SCAN_CODE = 1;

    // 人脸识别
    int FACE_RECOGNITION = 2;
  }

  public interface MessageCheckStatus {

    // 等待审核
    int WAIT_CHECK = 0;

    // 审核通过
    int PASS = 1;

    // 驳回
    int REJECT = 2;
  }

  public interface OAuth2Url {

    String OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token";

    String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";

    String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info";
  }

  public interface OAuth2Param {

    String OPENID = "appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    String ACCESS_TOKEN = "grant_type=client_credential&appid=%s&secret=%s";

    String USER_INFO = "access_token=%s&openid=%s";
  }

  // 中奖推送消息
  public static final String LOTTERY_MESSAGE = "恭喜你，中奖啦！";

  // 留言墙初始索引
  public static final int MESSAGE_DEFAULT_INDEX = 0;

  public static final String EMPTY_STR = "";

}
