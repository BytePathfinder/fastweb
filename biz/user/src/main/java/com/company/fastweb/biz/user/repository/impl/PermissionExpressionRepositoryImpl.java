package com.company.fastweb.biz.user.repository.impl;

import com.company.fastweb.biz.user.entity.PermissionExpression;
import com.company.fastweb.biz.user.mapper.PermissionExpressionMapper;
import com.company.fastweb.biz.user.repository.PermissionExpressionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限表达式Repository实现
 */
@Repository
@RequiredArgsConstructor
public class PermissionExpressionRepositoryImpl implements PermissionExpressionRepository {
    
    private final PermissionExpressionMapper mapper;
    
    @Override
    public Optional<PermissionExpression> findByPermissionId(Long permissionId) {
        return Optional.ofNullable(mapper.selectByPermissionId(permissionId));
    }
    
    @Override
    public Optional<PermissionExpression> findByPermissionCode(String permissionCode) {
        return Optional.ofNullable(mapper.selectByPermissionCode(permissionCode));
    }
    
    @Override
    public List<PermissionExpression> findAllEnabled() {
        return mapper.selectAllEnabled();
    }
    
    @Override
    public PermissionExpression save(PermissionExpression expression) {
        if (expression.getId() == null) {
            mapper.insert(expression);
        } else {
            mapper.updateById(expression);
        }
        return expression;
    }
    
    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }
}