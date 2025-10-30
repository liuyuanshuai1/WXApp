package com.familypoints.backend.dto;


import lombok.Data;

/**
 * API通用响应类
 * 封装所有API的统一响应格式
 */
@Data
public class ApiResponse<T> {

    /**
     * 响应码，0表示成功
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 获取响应码
     * @return 响应码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置响应码
     * @param code 响应码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取响应消息
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置响应消息
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取响应数据
     * @return 响应数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置响应数据
     * @param data 响应数据
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 构造成功响应
     * @param data 响应数据
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(0);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * 构造失败响应
     * @param code 错误码
     * @param message 错误消息
     * @return 失败响应对象
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        return response;
    }

}