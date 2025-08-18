package com.company.fastweb.core.exception.handler;

import com.company.fastweb.core.exception.BizException;
import com.company.fastweb.core.exception.SystemException;
import com.company.fastweb.core.exception.model.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author FastWeb
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(BizException.class)
    public ResponseEntity<ApiResult<Object>> handleBizException(BizException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        log.warn("业务异常: code={}, message={}, traceId={}, uri={}", 
            ex.getCode(), ex.getMessage(), traceId, request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.error(ex.getCode(), ex.getMessage()));
    }

    /**
     * 系统异常处理
     */
    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ApiResult<Object>> handleSystemException(SystemException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        log.error("系统异常: code={}, message={}, traceId={}, uri={}", 
            ex.getCode(), ex.getMessage(), traceId, request.getRequestURI(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResult.error(ex.getCode(), ex.getMessage()));
    }

    /**
     * 参数校验异常处理 - @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        
        log.warn("参数校验异常: message={}, traceId={}, uri={}", message, traceId, request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.error("VALIDATION_ERROR", message));
    }

    /**
     * 参数校验异常处理 - @RequestParam/@PathVariable
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResult<Object>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        String message = ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));
        
        log.warn("参数校验异常: message={}, traceId={}, uri={}", message, traceId, request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.error("VALIDATION_ERROR", message));
    }

    /**
     * 参数绑定异常处理 - @ModelAttribute
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResult<Object>> handleBindException(BindException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        
        log.warn("参数绑定异常: message={}, traceId={}, uri={}", message, traceId, request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.error("VALIDATION_ERROR", message));
    }

    /**
     * 非法参数异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        log.warn("非法参数异常: message={}, traceId={}, uri={}", ex.getMessage(), traceId, request.getRequestURI());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResult.error("ILLEGAL_ARGUMENT", ex.getMessage()));
    }

    /**
     * 空指针异常处理
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResult<Object>> handleNullPointerException(
            NullPointerException ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        log.error("空指针异常: traceId={}, uri={}", traceId, request.getRequestURI(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResult.error("NULL_POINTER", "系统内部错误，请稍后重试。traceId=" + traceId));
    }

    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult<Object>> handleException(Exception ex, HttpServletRequest request) {
        String traceId = MDC.get("traceId");
        log.error("未知异常: traceId={}, uri={}", traceId, request.getRequestURI(), ex);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResult.error("SYSTEM_ERROR", "系统异常，请稍后重试。traceId=" + traceId));
    }
}