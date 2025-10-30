package com.familypoints.backend.exception;

import com.familypoints.backend.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.Data;

/**
 * 全局异常处理器
 * 统一处理所有异常，返回标准的API响应格式
 */
@ControllerAdvice
@Data   
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * @param e 业务异常
     * @return 标准响应对象
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e) {
        ApiResponse<?> response = ApiResponse.error(e.getCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 处理其他所有异常
     * @param e 异常
     * @return 标准响应对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        // 记录异常日志
        e.printStackTrace();
        ApiResponse<?> response = ApiResponse.error(50000, "系统内部错误：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}