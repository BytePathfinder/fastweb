package com.company.fastweb.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果
 */
@Data
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<T> records;
    private final long total;
    private final long size;
    private final long current;
    private final long pages;

    public PageResult(IPage<T> page) {
        this(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
    }

    public PageResult(List<T> records, long total, long size, long current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
        this.pages = size == 0 ? 0 : (long) Math.ceil((double) total / size);
    }

    public static <T> PageResult<T> of(List<T> records, long total, long size, long current) {
        return new PageResult<>(records, total, size, current);
    }

    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page);
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(null, 0, 0, 0);
    }
}