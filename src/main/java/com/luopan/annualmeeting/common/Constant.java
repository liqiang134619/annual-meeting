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
    String MESSAGE_TASK_INTERVAL = "ANNUAL_MEETING:MESSAGE_TASK_INTERVAL";

    // 任务消息数
    String MESSAGE_TASK_NUM = "ANNUAL_MEETING:MESSAGE_TASK_NUM";

    // 上次读取消息的偏移量
    String MESSAGE_OFFSET = "ANNUAL_MEETING:MESSAGE_OFFSET";

    // 没有新消息时的上次读取消息的序号
    String NO_NEW_MESSAGE_OFFSET = "ANNUAL_MEETING:NO_NEW_MESSAGE_OFFSET";

    // 已被禁言人员ID
    String BANNED_PERSON_ID = "ANNUAL_MEETING:BANNED_PERSON_ID";

    // 投票开始时间
    String VOTE_START_TIME = "ANNUAL_MEETING:VOTE_START_TIME";

    // 投票结束时间
    String VOTE_END_TIME = "ANNUAL_MEETING:VOTE_END_TIME";

    // 单人投票次数
    String PERSON_VOTE_NUM = "ANNUAL_MEETING:PERSON_VOTE_NUM";

    // 年会开始时间
    String ANNUAL_MEETING_START_TIME = "ANNUAL_MEETING:ANNUAL_MEETING_START_TIME";

    // 年会结束时间
    String ANNUAL_MEETING_END_TIME = "ANNUAL_MEETING:ANNUAL_MEETING_END_TIME";

    // 留言审批标志
    String MESSAGE_CHECK_STATE = "ANNUAL_MEETING:MESSAGE_CHECK_STATE";
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

  /**
   * 中奖推送消息
   */
  public static final String LOTTERY_MESSAGE = "恭喜你，中奖啦！";

  /**
   * 留言墙初始索引
   */
  public static final int MESSAGE_DEFAULT_OFFSET = 0;

  public static final String EMPTY_STR = "";

  public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=utf-8";

  /**
   * 投票
   */
  // 个人投票数
  public static final int PERSON_VOTE_NUM = 1;

  // 投票消息推送间隔(秒)
  public static final int VOTE_TASK_INTERVAL = 5;

  /**
   * 微信
   */
  // 公众号appId
  public static final String WECHAT_APP_ID = "wxcac24d26be875055";

  // 公众号appSecret
  public static final String WECHAT_APP_SECRET = "2394ac32e9591dbabd5644f6377e9d58";

  /**
   * 留言墙任务
   */
  // 留言墙消息推送间隔(秒)
  public static final int MESSAGE_TASK_INTERVAL = 10;

  // 留言墙消息推送数量
  public static final int MESSAGE_TASK_NUM = 10;

}
