package com.familypoints.backend.utils;

import com.familypoints.backend.dto.ApiResponse;

/**
 * 响应工具类
 * 用于快速返回标准格式的API响应
 */
public class ResponseUtils {

    /**
     * 返回成功响应
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }

    /**
     * 返回带消息的成功响应
     * @param message 响应消息
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        // 创建新的ApiResponse对象并设置所有字段
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(0);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    /**
     * 返回成功响应（无数据）
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.success(null);
    }

    /**
     * 返回错误响应
     * @param code 错误码
     * @param message 错误消息
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.error(code, message);
    }

    /**
     * 返回参数错误响应
     * @param message 错误消息
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> paramError(String message) {
        return error(ErrorCode.PARAM_ERROR, message);
    }

    /**
     * 返回未授权错误响应
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> unauthorized() {
        return error(ErrorCode.UNAUTHORIZED, "未授权访问");
    }

    /**
     * 返回权限不足错误响应
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> forbidden() {
        return error(ErrorCode.FORBIDDEN, "权限不足");
    }

    /**
     * 返回资源不存在错误响应
     * @param resource 资源名称
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> notFound(String resource) {
        return error(ErrorCode.RESOURCE_NOT_FOUND, resource + "不存在");
    }

    /**
     * 返回积分不足错误响应
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> insufficientPoints() {
        return error(ErrorCode.INSUFFICIENT_POINTS, "积分不足");
    }

    /**
     * 返回库存不足错误响应
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> insufficientStock() {
        return error(ErrorCode.INSUFFICIENT_STOCK, "库存不足");
    }
}