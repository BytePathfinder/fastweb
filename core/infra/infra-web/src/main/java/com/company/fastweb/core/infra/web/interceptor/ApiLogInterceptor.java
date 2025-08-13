package com.company.fastweb.core.infra.web.interceptor;

import com.company.fastweb.core.infra.web.config.WebProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * API日志拦截器
 *
 * @author fastweb
 */
@Slf4j
@RequiredArgsConstructor
public class ApiLogInterceptor implements HandlerInterceptor {

    private final WebProperties webProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!webProperties.getApiLog().isEnabled()) {
            return true;
        }

        // 生成请求ID
        String requestId = UUID.randomUUID().toString().replace("-", "");
        MDC.put("requestId", requestId);
        
        // 记录请求开始时间
        request.setAttribute("startTime", System.currentTimeMillis());
        
        // 记录请求信息
        log.info("API Request - Method: {}, URI: {}, RemoteAddr: {}", 
                request.getMethod(), request.getRequestURI(), getClientIpAddress(request));
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                              Object handler, Exception ex) {
        if (!webProperties.getApiLog().isEnabled()) {
            return;
        }

        try {
            Long startTime = (Long) request.getAttribute("startTime");
            if (startTime != null) {
                long duration = System.currentTimeMillis() - startTime;
                log.info("API Response - Status: {}, Duration: {}ms", response.getStatus(), duration);
            }
        } finally {
            MDC.clear();
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}