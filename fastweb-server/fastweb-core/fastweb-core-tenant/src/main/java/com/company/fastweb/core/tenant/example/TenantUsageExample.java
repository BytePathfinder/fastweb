package com.company.fastweb.core.tenant.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.fastweb.core.data.model.BaseEntity;
import com.company.fastweb.core.tenant.cache.TenantAwareCacheKeyGenerator;
import com.company.fastweb.core.tenant.context.TenantContextHolder;
import com.company.fastweb.core.tenant.datasource.TenantDataSourceRouter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 多租户使用示例
 * 展示如何在实际业务代码中使用多租户功能
 */
public class TenantUsageExample {

    /**
     * 示例实体类 - 用户
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class User extends BaseEntity implements Serializable {
        
        private Long userId;
        
        private String username;
        
        private String email;
        
        private String phone;
        
        /**
         * 租户ID - 多租户字段
         * 在字段模式下，MyBatis-Plus会自动为此字段添加条件
         */
        private String tenantId;
    }

    /**
     * 示例控制器
     */
    @RestController
    @RequestMapping("/api/users")
    public static class UserController {
        
        @Autowired
        private UserService userService;
        
        /**
         * 查询用户列表
         * 自动应用租户过滤，只返回当前租户的用户
         */
        @GetMapping
        public List<User> listUsers() {
            return userService.list();
        }
        
        /**
         * 根据ID查询用户
         * 使用租户感知缓存
         */
        @GetMapping("/{id}")
        public User getUser(@PathVariable Long id) {
            return userService.getById(id);
        }
        
        /**
         * 创建用户
         * 自动设置租户ID
         */
        @PostMapping
        public ResponseEntity<User> createUser(@RequestBody User user) {
            boolean success = userService.save(user);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            }
            return ResponseEntity.badRequest().build();
        }
        
        /**
         * 更新用户
         * 只能更新当前租户的用户
         */
        @PutMapping("/{id}")
        public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
            user.setId(id);
            boolean success = userService.updateById(user);
            if (success) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        }
        
        /**
         * 删除用户
         * 只能删除当前租户的用户
         */
        @DeleteMapping("/{id}")
        public void deleteUser(@PathVariable Long id) {
            userService.removeById(id);
        }
        
        /**
         * 管理员接口 - 查询所有租户的用户
         * 需要特殊处理，忽略租户过滤
         */
        @GetMapping("/admin/all")
        public List<User> listAllUsers() {
            return userService.listAllTenants();
        }
    }

    /**
     * 示例服务类
     */
    @Service
    public static class UserService extends ServiceImpl<UserMapper, User> {
        
        @Autowired
        private TenantDataSourceRouter dataSourceRouter;
        
        /**
         * 查询用户列表
         * 自动应用租户过滤
         */
        @Override
        public List<User> list() {
            return baseMapper.selectList(null);
        }
        
        /**
         * 根据ID查询用户
         * 使用租户感知缓存
         */
        @Override
        @Cacheable(value = "userCache", keyGenerator = "tenantAwareCacheKeyGenerator")
        public User getById(Serializable id) {
            return baseMapper.selectById(id);
        }
        
        /**
         * 保存用户
         * 自动设置租户ID
         */
        @Override
        public boolean save(User user) {
            // 租户ID会通过MetaObjectHandler自动填充
            return super.save(user);
        }
        
        /**
         * 根据用户名查询用户
         * 自动应用租户过滤
         */
        public User getByUsername(String username) {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username);
            return baseMapper.selectOne(wrapper);
        }
        
        /**
         * 查询所有租户的用户（管理员功能）
         * 需要特殊处理，忽略租户过滤
         */
        public List<User> listAllTenants() {
            // 方法1: 临时清除租户上下文
            String currentTenantId = TenantContextHolder.getTenantId();
            try {
                TenantContextHolder.clear();
                return baseMapper.selectList(null);
            } finally {
                TenantContextHolder.setTenantId(currentTenantId);
            }
        }
        
        /**
         * 跨租户数据统计
         */
        public void crossTenantStatistics() {
            // 切换到主数据源进行统计
            dataSourceRouter.routeToMasterDataSource();
            try {
                // 执行跨租户统计逻辑
                // ...
            } finally {
                dataSourceRouter.clearDataSourceRoute();
            }
        }
    }

    /**
     * 示例Mapper接口
     */
    public interface UserMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<User> {
        
        /**
         * 自定义查询方法
         * 会自动应用租户过滤
         */
        List<User> selectActiveUsers();
        
        /**
         * 统计用户数量
         * 会自动应用租户过滤
         */
        Long countUsers();
    }

    /**
     * 租户切换服务示例
     */
    @Service
    public static class TenantSwitchService {
        
        @Autowired
        private TenantDataSourceRouter dataSourceRouter;
        
        /**
         * 切换到指定租户
         */
        public void switchToTenant(String tenantId) {
            TenantContextHolder.setTenantId(tenantId);
            dataSourceRouter.routeToTenantDataSource(tenantId);
        }
        
        /**
         * 切换到主数据源
         */
        public void switchToMaster() {
            TenantContextHolder.clear();
            dataSourceRouter.routeToMasterDataSource();
        }
        
        /**
         * 执行跨租户操作
         */
        public void executeCrossTenantOperation() {
            String originalTenantId = TenantContextHolder.getTenantId();
            
            try {
                // 切换到租户1
                switchToTenant("1001");
                // 执行租户1的操作
                
                // 切换到租户2
                switchToTenant("1002");
                // 执行租户2的操作
                
                // 切换到主数据源
                switchToMaster();
                // 执行主数据源操作
                
            } finally {
                // 恢复原始租户上下文
                TenantContextHolder.setTenantId(originalTenantId);
            }
        }
    }

    /**
     * 租户数据初始化示例
     */
    @Service
    public static class TenantDataInitService {
        
        @Autowired
        private UserService userService;
        
        @Autowired
        private TenantSwitchService tenantSwitchService;
        
        /**
         * 为新租户初始化数据
         */
        public void initTenantData(String tenantId) {
            // 切换到指定租户
            tenantSwitchService.switchToTenant(tenantId);
            
            try {
                // 创建默认管理员用户
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@" + tenantId + ".com");
                admin.setPhone("13800000000");
                userService.save(admin);
                
                // 创建其他初始数据...
                
            } finally {
                // 清理租户上下文
                TenantContextHolder.clear();
            }
        }
        
        /**
         * 批量为多个租户初始化数据
         */
        public void batchInitTenantData(List<String> tenantIds) {
            for (String tenantId : tenantIds) {
                initTenantData(tenantId);
            }
        }
    }
}