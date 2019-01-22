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

    String ANNUAL_MEETING_PREFIX = "ANNUAL_MEETING:";

    // 任务间隔
    String MESSAGE_TASK_INTERVAL = ANNUAL_MEETING_PREFIX + "MESSAGE_TASK_INTERVAL";

    // 任务消息数
    String MESSAGE_TASK_NUM = ANNUAL_MEETING_PREFIX + "MESSAGE_TASK_NUM";

    // 上次读取消息的偏移量
    String MESSAGE_OFFSET = ANNUAL_MEETING_PREFIX + "MESSAGE_OFFSET";

    // 没有新消息时的上次读取消息的序号
    String NO_NEW_MESSAGE_OFFSET = ANNUAL_MEETING_PREFIX + "NO_NEW_MESSAGE_OFFSET";

    // 已被禁言人员ID
    String BANNED_PERSON_ID = ANNUAL_MEETING_PREFIX + "BANNED_PERSON_ID";

    // 年会状态
    String ANNUAL_MEETING_STATE = ANNUAL_MEETING_PREFIX + "ANNUAL_MEETING_STATE";

    // 留言审批标志
    String MESSAGE_CHECK_STATE = ANNUAL_MEETING_PREFIX + "MESSAGE_CHECK_STATE";

    // 可投票节目
    String VOTEABLE_SHOW = ANNUAL_MEETING_PREFIX + "VOTEABLE_SHOW";

    // 节目投票数
    String SHOW_VOTE_NUM = ANNUAL_MEETING_PREFIX + "SHOW_VOTE_NUM";

    // 单人投票次数
    String PERSON_VOTE_NUM = ANNUAL_MEETING_PREFIX + "PERSON_VOTE_NUM";

    // 企业
    String COMPANY_MAP = ANNUAL_MEETING_PREFIX + "COMPANY_MAP";
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

    String AUTH = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    String USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
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
  // 单人投票次数
  public static final int PERSON_VOTE_NUM = 3;

  // 投票消息推送间隔(秒)
  public static final int VOTE_TASK_INTERVAL = 5;

  /**
   * 留言墙任务
   */
  // 留言墙消息推送间隔(秒)
  public static final int MESSAGE_TASK_INTERVAL = 10;

  // 留言墙消息推送数量
  public static final int MESSAGE_TASK_NUM = 10;

  // 默认消息审批状态
  public static final int MESSAGE_CHECK_STATUS = 0;

  /**
   * 通用分隔符
   */
  public static final String SPLITTER_COLON = ":";
  public static final String SPLITTER_COMMA = ",";
  public static final String SPLITTER_SEMICOLON = ";";

  /**
   * 通用参数
   */
  public static final String COMMON_PARAM_COMPANY_ID = "companyId";

  /**
   * 刷新加载操作
   */
  // 加载
  public static final String ACT_LOAD = "load";

  // 刷新
  public static final String ACT_REFRESH = "refresh";

  // 单次操作返回消息数
  public static final int ACT_MESSAGE_NUM = 10;

  /**
   * 企业id
   */
  // 罗盘
  public static final long COMPANY_ID_LP = 1;

  // 招行
  public static final long COMPANY_ID_ZH = 2;

}
