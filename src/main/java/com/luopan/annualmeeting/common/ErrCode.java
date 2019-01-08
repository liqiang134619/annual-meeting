package com.luopan.annualmeeting.common;

public enum ErrCode {

  SUCCESS(0, "接口调用成功"),

  SERVER_ERROR(500, "服务异常"),

  // 奖品 1001-1050
  NO_REWARD(1001, "未找到奖品"),

  // 人员 1051-1100
  NO_PERSON(1051, "未找到人员"),
  NO_ENOUGH_PERSON(1052, "参与抽奖人数不足"),
  HAD_SIGN_IN(1053, "重复签到"),
  INVALID_CODE(1054, "无效CODE"),

  // 点赞 1101-1120
  HAD_PRAISE(1101, "重复点赞"),

  // 评分 1121-1140
  HAD_SCORE(1121, "重复评分"),

  // 任务 1141-1160
  TASK_MODIFY_INTERVAL_ERROR(1141, "修改任务间隔错误"),
  TASK_MODIFY_MESSAGE_NUM_ERROR(1142, "修改任务消息数错误"),

  // 用户 1161-1200
  USER_NOT_FOUND(1161, "未找到用户"),
  USER_NO_PERMISSION(1162, "权限不足"),

  // 留言 1201-1250
  MESSAGE_NOT_FOUND(1201, "未找到留言"),

  // 投票 1251-1300
  VOTE_NOT_STARTED(1251, "投票尚未开始"),
  VOTE_ENDED(1252, "投票已结束"),
  HAD_VOTE(1253, "重复投票"),
  NO_VOTE_NUM(1254, "无投票次数"),
  ;

  private int errCode;
  private String errMsg;

  ErrCode(int errCode, String errMsg) {
    this.errCode = errCode;
    this.errMsg = errMsg;
  }

  public int getErrCode() {
    return errCode;
  }

  public String getErrMsg() {
    return errMsg;
  }
}
