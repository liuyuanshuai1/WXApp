package com.familypoints.backend.utils;

/**
 * 错误码常量类
 * 统一管理系统中的错误码
 */
public class ErrorCode {

    // 成功
    public static final int SUCCESS = 0;

    // 参数错误
    public static final int PARAM_ERROR = 10001;
    // 未认证/登录失效
    public static final int UNAUTHORIZED = 10002;
    // 权限不足
    public static final int FORBIDDEN = 10003;
    // 资源不存在
    public static final int RESOURCE_NOT_FOUND = 10004;
    // 资源已存在
    public static final int RESOURCE_EXIST = 10005;

    // 积分不足
    public static final int INSUFFICIENT_POINTS = 20001;
    // 库存不足
    public static final int INSUFFICIENT_STOCK = 20002;

    // 数据库操作失败
    public static final int DATABASE_ERROR = 30001;

    // 服务端错误
    public static final int SERVER_ERROR = 50000;

    // 家庭相关错误
    public static final int FAMILY_CODE_EXPIRED = 40001;
    public static final int FAMILY_MEMBER_FULL = 40002;
    public static final int ALREADY_IN_FAMILY = 40003;
    public static final int USER_HAS_FAMILY = 40004;
    public static final int FAMILY_NOT_FOUND = 40005;

    // 任务相关错误
    public static final int TASK_EXPIRED = 50001;
    public static final int TASK_NOT_AVAILABLE = 50002;
    public static final int TASK_ALREADY_SUBMITTED = 50003;

    // 奖励相关错误
    public static final int REWARD_NOT_AVAILABLE = 60001;

}