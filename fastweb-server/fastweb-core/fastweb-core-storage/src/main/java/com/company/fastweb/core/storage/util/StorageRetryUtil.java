package com.company.fastweb.core.storage.util;

import com.company.fastweb.core.storage.exception.StorageException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * 存储重试工具类
 * 提供指数退避重试机制
 *
 * @author FastWeb
 */
@Slf4j
public class StorageRetryUtil {

    /**
     * 默认最大重试次数
     */
    private static final int DEFAULT_MAX_RETRIES = 3;

    /**
     * 默认基础延迟时间（毫秒）
     */
    private static final long DEFAULT_BASE_DELAY = 1000L;

    /**
     * 默认最大延迟时间（毫秒）
     */
    private static final long DEFAULT_MAX_DELAY = 10000L;

    /**
     * 执行带重试的操作
     *
     * @param operation 要执行的操作
     * @param operationName 操作名称（用于日志）
     * @param <T> 返回类型
     * @return 操作结果
     */
    public static <T> T executeWithRetry(Supplier<T> operation, String operationName) {
        return executeWithRetry(operation, operationName, DEFAULT_MAX_RETRIES, DEFAULT_BASE_DELAY, DEFAULT_MAX_DELAY);
    }

    /**
     * 执行带重试的操作
     *
     * @param operation 要执行的操作
     * @param operationName 操作名称（用于日志）
     * @param maxRetries 最大重试次数
     * @param <T> 返回类型
     * @return 操作结果
     */
    public static <T> T executeWithRetry(Supplier<T> operation, String operationName, int maxRetries) {
        return executeWithRetry(operation, operationName, maxRetries, DEFAULT_BASE_DELAY, DEFAULT_MAX_DELAY);
    }

    /**
     * 执行带重试的操作（完整参数）
     *
     * @param operation 要执行的操作
     * @param operationName 操作名称（用于日志）
     * @param maxRetries 最大重试次数
     * @param baseDelay 基础延迟时间（毫秒）
     * @param maxDelay 最大延迟时间（毫秒）
     * @param <T> 返回类型
     * @return 操作结果
     */
    public static <T> T executeWithRetry(Supplier<T> operation, String operationName, 
                                        int maxRetries, long baseDelay, long maxDelay) {
        Exception lastException = null;
        
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                if (attempt > 0) {
                    log.info("重试执行存储操作: operation={}, attempt={}/{}", operationName, attempt, maxRetries);
                }
                
                return operation.get();
                
            } catch (Exception e) {
                lastException = e;
                
                if (attempt == maxRetries) {
                    log.error("存储操作最终失败: operation={}, attempts={}, error={}", 
                            operationName, attempt + 1, e.getMessage());
                    break;
                }
                
                if (!isRetryableException(e)) {
                    log.warn("存储操作遇到不可重试异常: operation={}, error={}", operationName, e.getMessage());
                    break;
                }
                
                long delay = calculateDelay(attempt, baseDelay, maxDelay);
                log.warn("存储操作失败，将在{}ms后重试: operation={}, attempt={}/{}, error={}", 
                        delay, operationName, attempt + 1, maxRetries, e.getMessage());
                
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new StorageException("存储操作被中断: " + operationName, ie);
                }
            }
        }
        
        // 所有重试都失败了，抛出最后一个异常
        if (lastException instanceof StorageException) {
            throw (StorageException) lastException;
        } else {
            throw new StorageException("存储操作失败: " + operationName, lastException);
        }
    }

    /**
     * 执行带重试的无返回值操作
     *
     * @param operation 要执行的操作
     * @param operationName 操作名称（用于日志）
     */
    public static void executeWithRetry(Runnable operation, String operationName) {
        executeWithRetry(() -> {
            operation.run();
            return null;
        }, operationName);
    }

    /**
     * 执行带重试的无返回值操作
     *
     * @param operation 要执行的操作
     * @param operationName 操作名称（用于日志）
     * @param maxRetries 最大重试次数
     */
    public static void executeWithRetry(Runnable operation, String operationName, int maxRetries) {
        executeWithRetry(() -> {
            operation.run();
            return null;
        }, operationName, maxRetries);
    }

    /**
     * 判断异常是否可重试
     *
     * @param exception 异常
     * @return 是否可重试
     */
    private static boolean isRetryableException(Exception exception) {
        // 网络相关异常通常可以重试
        String message = exception.getMessage();
        if (message != null) {
            String lowerMessage = message.toLowerCase();
            if (lowerMessage.contains("connection") || 
                lowerMessage.contains("timeout") || 
                lowerMessage.contains("network") ||
                lowerMessage.contains("socket") ||
                lowerMessage.contains("refused") ||
                lowerMessage.contains("reset")) {
                return true;
            }
        }
        
        // StorageException 中的特定错误码可以重试
        if (exception instanceof StorageException) {
            StorageException se = (StorageException) exception;
            String code = se.getCode();
            return StorageException.STORAGE_CONNECTION_ERROR.equals(code) ||
                   StorageException.FILE_UPLOAD_ERROR.equals(code) ||
                   StorageException.FILE_DOWNLOAD_ERROR.equals(code);
        }
        
        // 其他异常类型的判断
        return exception instanceof java.net.SocketException ||
               exception instanceof java.net.ConnectException ||
               exception instanceof java.net.SocketTimeoutException ||
               exception instanceof java.io.IOException;
    }

    /**
     * 计算延迟时间（指数退避 + 随机抖动）
     *
     * @param attempt 当前重试次数（从0开始）
     * @param baseDelay 基础延迟时间
     * @param maxDelay 最大延迟时间
     * @return 延迟时间（毫秒）
     */
    private static long calculateDelay(int attempt, long baseDelay, long maxDelay) {
        // 指数退避：baseDelay * 2^attempt
        long exponentialDelay = baseDelay * (1L << attempt);
        
        // 限制最大延迟
        long delay = Math.min(exponentialDelay, maxDelay);
        
        // 添加随机抖动（±25%）
        double jitter = 0.75 + (ThreadLocalRandom.current().nextDouble() * 0.5);
        
        return (long) (delay * jitter);
    }
}