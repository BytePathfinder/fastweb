package com.company.fastweb.core.monitor.service.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricDescriptor;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricNamesDescriptor;
import org.springframework.stereotype.Service;

import com.company.fastweb.core.monitor.service.MonitorService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认监控服务实现
 *
 * @author FastWeb
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMonitorServiceImpl implements MonitorService {

    private final MeterRegistry meterRegistry;
    private final HealthEndpoint healthEndpoint;
    private final MetricsEndpoint metricsEndpoint;
    private final DataSource dataSource;

    // 缓存仪表盘值
    private final Map<String, AtomicReference<Double>> gaugeState = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        systemInfo.put("osName", osBean.getName());
        systemInfo.put("osVersion", osBean.getVersion());
        systemInfo.put("osArch", osBean.getArch());
        systemInfo.put("availableProcessors", osBean.getAvailableProcessors());

        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            systemInfo.put("totalPhysicalMemory", sunOsBean.getTotalPhysicalMemorySize());
            systemInfo.put("freePhysicalMemory", sunOsBean.getFreePhysicalMemorySize());
            systemInfo.put("totalSwapSpace", sunOsBean.getTotalSwapSpaceSize());
            systemInfo.put("freeSwapSpace", sunOsBean.getFreeSwapSpaceSize());
            systemInfo.put("systemCpuLoad", sunOsBean.getSystemCpuLoad());
            systemInfo.put("processCpuLoad", sunOsBean.getProcessCpuLoad());
        }

        return systemInfo;
    }

    @Override
    public Map<String, Object> getJvmInfo() {
        Map<String, Object> jvmInfo = new HashMap<>();

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        jvmInfo.put("jvmName", runtimeBean.getVmName());
        jvmInfo.put("jvmVersion", runtimeBean.getVmVersion());
        jvmInfo.put("jvmVendor", runtimeBean.getVmVendor());
        jvmInfo.put("startTime", runtimeBean.getStartTime());
        jvmInfo.put("uptime", runtimeBean.getUptime());
        jvmInfo.put("inputArguments", runtimeBean.getInputArguments());

        // Java版本信息
        jvmInfo.put("javaVersion", System.getProperty("java.version"));
        jvmInfo.put("javaVendor", System.getProperty("java.vendor"));
        jvmInfo.put("javaHome", System.getProperty("java.home"));

        return jvmInfo;
    }

    @Override
    public Map<String, Object> getMemoryInfo() {
        Map<String, Object> memoryInfo = new HashMap<>();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        // 堆内存
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        Map<String, Object> heap = new HashMap<>();
        heap.put("init", heapMemory.getInit());
        heap.put("used", heapMemory.getUsed());
        heap.put("committed", heapMemory.getCommitted());
        heap.put("max", heapMemory.getMax());
        heap.put("usagePercent", (double) heapMemory.getUsed() / heapMemory.getMax() * 100);
        memoryInfo.put("heap", heap);

        // 非堆内存
        MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();
        Map<String, Object> nonHeap = new HashMap<>();
        nonHeap.put("init", nonHeapMemory.getInit());
        nonHeap.put("used", nonHeapMemory.getUsed());
        nonHeap.put("committed", nonHeapMemory.getCommitted());
        nonHeap.put("max", nonHeapMemory.getMax());
        memoryInfo.put("nonHeap", nonHeap);

        return memoryInfo;
    }

    @Override
    public Map<String, Object> getCpuInfo() {
        Map<String, Object> cpuInfo = new HashMap<>();

        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        cpuInfo.put("availableProcessors", osBean.getAvailableProcessors());
        cpuInfo.put("systemLoadAverage", osBean.getSystemLoadAverage());

        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            cpuInfo.put("systemCpuLoad", sunOsBean.getSystemCpuLoad());
            cpuInfo.put("processCpuLoad", sunOsBean.getProcessCpuLoad());
            cpuInfo.put("processCpuTime", sunOsBean.getProcessCpuTime());
        }

        return cpuInfo;
    }

    @Override
    public Map<String, Object> getDiskInfo() {
        Map<String, Object> diskInfo = new HashMap<>();

        try {
            java.io.File[] roots = java.io.File.listRoots();
            for (java.io.File root : roots) {
                Map<String, Object> rootInfo = new HashMap<>();
                rootInfo.put("totalSpace", root.getTotalSpace());
                rootInfo.put("freeSpace", root.getFreeSpace());
                rootInfo.put("usableSpace", root.getUsableSpace());
                rootInfo.put("usagePercent",
                        (double) (root.getTotalSpace() - root.getFreeSpace()) / root.getTotalSpace() * 100);
                diskInfo.put(root.getPath(), rootInfo);
            }
        } catch (Exception e) {
            log.error("获取磁盘信息失败", e);
        }

        return diskInfo;
    }

    @Override
    public Map<String, Object> getNetworkInfo() {
        Map<String, Object> networkInfo = new HashMap<>();

        try {
            java.net.NetworkInterface.getNetworkInterfaces().asIterator().forEachRemaining(ni -> {
                try {
                    if (ni.isUp() && !ni.isLoopback()) {
                        Map<String, Object> interfaceInfo = new HashMap<>();
                        interfaceInfo.put("displayName", ni.getDisplayName());
                        interfaceInfo.put("mtu", ni.getMTU());
                        interfaceInfo.put("isVirtual", ni.isVirtual());
                        interfaceInfo.put("supportsMulticast", ni.supportsMulticast());

                        // MAC地址
                        byte[] mac = ni.getHardwareAddress();
                        if (mac != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < mac.length; i++) {
                                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                            }
                            interfaceInfo.put("macAddress", sb.toString());
                        }

                        networkInfo.put(ni.getName(), interfaceInfo);
                    }
                } catch (Exception e) {
                    log.debug("获取网络接口信息失败: {}", ni.getName(), e);
                }
            });
        } catch (Exception e) {
            log.error("获取网络信息失败", e);
        }

        return networkInfo;
    }

    @Override
    public Map<String, Object> getDatabaseInfo() {
        Map<String, Object> dbInfo = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            dbInfo.put("databaseProductName", metaData.getDatabaseProductName());
            dbInfo.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            dbInfo.put("driverName", metaData.getDriverName());
            dbInfo.put("driverVersion", metaData.getDriverVersion());
            dbInfo.put("url", metaData.getURL());
            dbInfo.put("userName", metaData.getUserName());
            dbInfo.put("maxConnections", metaData.getMaxConnections());
            dbInfo.put("isReadOnly", connection.isReadOnly());
            dbInfo.put("autoCommit", connection.getAutoCommit());
            dbInfo.put("transactionIsolation", connection.getTransactionIsolation());
        } catch (Exception e) {
            log.error("获取数据库信息失败", e);
            dbInfo.put("error", e.getMessage());
        }

        return dbInfo;
    }

    @Override
    public Map<String, Object> getCacheInfo() {
        Map<String, Object> cacheInfo = new HashMap<>();

        try {
            // 这里可以集成具体的缓存实现信息
            cacheInfo.put("type", "Redis");
            cacheInfo.put("status", "connected");
        } catch (Exception e) {
            log.error("获取缓存信息失败", e);
            cacheInfo.put("error", e.getMessage());
        }

        return cacheInfo;
    }

    @Override
    public Map<String, Object> getApplicationInfo() {
        Map<String, Object> appInfo = new HashMap<>();

        appInfo.put("name", "FastWeb");
        appInfo.put("version", "1.0.0");
        appInfo.put("profiles", System.getProperty("spring.profiles.active", "default"));
        appInfo.put("encoding", System.getProperty("file.encoding"));
        appInfo.put("timezone", System.getProperty("user.timezone"));
        appInfo.put("workingDirectory", System.getProperty("user.dir"));
        appInfo.put("tempDirectory", System.getProperty("java.io.tmpdir"));

        return appInfo;
    }

    @Override
    public Map<String, Object> getHealthStatus() {
        try {
            HealthComponent healthComponent = healthEndpoint.health();
            Map<String, Object> healthStatus = new HashMap<>();
            healthStatus.put("status", healthComponent.getStatus());

            if (healthComponent instanceof org.springframework.boot.actuate.health.Health) {
                org.springframework.boot.actuate.health.Health health = (org.springframework.boot.actuate.health.Health) healthComponent;
                healthStatus.putAll(health.getDetails());
            }

            return healthStatus;
        } catch (Exception e) {
            log.error("获取健康状态失败", e);
            Map<String, Object> errorStatus = new HashMap<>();
            errorStatus.put("status", "DOWN");
            errorStatus.put("error", e.getMessage());
            return errorStatus;
        }
    }

    @Override
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        try {
            // 获取常用指标
            MetricNamesDescriptor names = metricsEndpoint.listNames();
            for (String name : names.getNames()) {
                if (name.startsWith("jvm.") || name.startsWith("system.") ||
                        name.startsWith("http.") || name.startsWith("jdbc.")) {
                    try {
                        MetricDescriptor metric = metricsEndpoint.metric(name, null);
                        metrics.put(name, metric);
                    } catch (Exception e) {
                        log.debug("获取指标失败: {}", name, e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取指标失败", e);
        }

        return metrics;
    }

    @Override
    public void recordMetric(String name, double value, String... tags) {
        try {
            meterRegistry.gauge(name, io.micrometer.core.instrument.Tags.of(tags), value);
        } catch (Exception e) {
            log.error("记录指标失败: name={}, value={}", name, value, e);
        }
    }

    @Override
    public void incrementCounter(String name, String... tags) {
        try {
            Counter.builder(name)
                    .tags(tags)
                    .register(meterRegistry)
                    .increment();
        } catch (Exception e) {
            log.error("增加计数器失败: name={}", name, e);
        }
    }

    @Override
    public void recordTimer(String name, long duration, String... tags) {
        try {
            Timer.builder(name)
                    .tags(tags)
                    .register(meterRegistry)
                    .record(Duration.ofMillis(duration));
        } catch (Exception e) {
            log.error("记录计时器失败: name={}, duration={}", name, duration, e);
        }
    }

    @Override
    public void setGauge(String name, double value, String... tags) {
        try {
            String key = name + ":" + String.join(",", tags);
            gaugeState.computeIfAbsent(key, k -> {
                AtomicReference<Double> state = new AtomicReference<>(value);
                Gauge.builder(name, state, AtomicReference::get)
                        .tags(tags)
                        .register(meterRegistry);
                return state;
            }).set(value);
        } catch (Exception e) {
            log.error("设置仪表盘失败: name={}, value={}", name, value, e);
        }
    }
}