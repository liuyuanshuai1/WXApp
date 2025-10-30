package com.familypoints.backend.exception;

import lombok.Data;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况
 */
@Data
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    /**
     * 获取错误码
     * @return 错误码
     */
    public int getCode() {
        return code;
    }

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}