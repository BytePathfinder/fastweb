package com.company.fastweb.core.cache.converter;

import com.company.fastweb.core.cache.model.dto.CacheInfoDTO;
import com.company.fastweb.core.cache.model.dto.CacheStatisticsDTO;
import com.company.fastweb.core.cache.model.dto.LockInfoDTO;
import com.company.fastweb.core.cache.model.form.CacheSetForm;
import com.company.fastweb.core.cache.model.form.LockOperationForm;
import com.company.fastweb.core.cache.model.vo.CacheInfoVO;
import com.company.fastweb.core.cache.model.vo.CacheStatisticsVO;
import com.company.fastweb.core.cache.model.vo.LockInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

/**
 * 缓存对象转换器
 *
 * @author FastWeb
 */
@Mapper(componentModel = "spring")
public interface CacheConverter {

    /**
     * 表单转DTO
     */
    CacheInfoDTO toDTO(CacheSetForm form);

    /**
     * DTO转VO
     */
    CacheInfoVO toVO(CacheInfoDTO dto);

    /**
     * DTO列表转VO列表
     */
    List<CacheInfoVO> toVOList(List<CacheInfoDTO> dtoList);

    /**
     * 统计DTO转VO
     */
    @Mapping(target = "usedMemoryFormatted", source = "usedMemory", qualifiedByName = "formatMemory")
    @Mapping(target = "maxMemoryFormatted", source = "maxMemory", qualifiedByName = "formatMemory")
    @Mapping(target = "memoryUsageRate", source = "memoryUsageRate", qualifiedByName = "formatPercentage")
    @Mapping(target = "hitRate", source = "hitRate", qualifiedByName = "formatPercentage")
    @Mapping(target = "uptime", source = "uptime", qualifiedByName = "formatDuration")
    @Mapping(target = "status", constant = "RUNNING")
    CacheStatisticsVO toStatisticsVO(CacheStatisticsDTO dto);

    /**
     * 格式化内存大小
     */
    @Named("formatMemory")
    default String formatMemory(Long bytes) {
        if (bytes == null || bytes == 0) {
            return "0 B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = bytes.doubleValue();
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(size) + " " + units[unitIndex];
    }

    /**
     * 格式化百分比
     */
    @Named("formatPercentage")
    default String formatPercentage(Double value) {
        if (value == null) {
            return "0.00%";
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value * 100) + "%";
    }

    /**
     * 格式化持续时间
     */
    @Named("formatDuration")
    default String formatDuration(Long seconds) {
        if (seconds == null || seconds == 0) {
            return "0秒";
        }
        
        Duration duration = Duration.ofSeconds(seconds);
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long secs = duration.getSeconds() % 60;
        
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append("天");
        }
        if (hours > 0) {
            sb.append(hours).append("小时");
        }
        if (minutes > 0) {
            sb.append(minutes).append("分钟");
        }
        if (secs > 0 || sb.length() == 0) {
            sb.append(secs).append("秒");
        }
        
        return sb.toString();
    }

    // ========== 分布式锁相关转换 ==========

    /**
     * LockOperationForm -> LockInfoDTO
     */
    LockInfoDTO toDTO(LockOperationForm form);

    /**
     * LockInfoDTO -> LockInfoVO
     */
    @Mapping(target = "remainingTimeFormatted", source = "remainingTime", qualifiedByName = "formatTime")
    @Mapping(target = "leaseTimeFormatted", source = "leaseTime", qualifiedByName = "formatTime")
    @Mapping(target = "statusLabel", source = "status", qualifiedByName = "formatStatusLabel")
    LockInfoVO toVO(LockInfoDTO dto);

    /**
     * 格式化时间
     */
    @Named("formatTime")
    default String formatTime(Long milliseconds) {
        if (milliseconds == null || milliseconds <= 0) {
            return "0ms";
        }
        
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return days + "天" + (hours % 24) + "小时";
        } else if (hours > 0) {
            return hours + "小时" + (minutes % 60) + "分钟";
        } else if (minutes > 0) {
            return minutes + "分钟" + (seconds % 60) + "秒";
        } else {
            return seconds + "秒";
        }
    }

    /**
     * 格式化状态标签
     */
    @Named("formatStatusLabel")
    default String formatStatusLabel(String status) {
        if (status == null) {
            return "未知";
        }
        return switch (status.toLowerCase()) {
            case "locked" -> "已锁定";
            case "unlocked" -> "已解锁";
            case "expired" -> "已过期";
            case "waiting" -> "等待中";
            default -> status;
        };
    }
}