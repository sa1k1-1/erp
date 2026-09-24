package com.aierp.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    // 总记录数
    private Long total;

    // 总页数
    private Long pages;

    // 当前页
    private Long pageNum;

    // 每页数量
    private Long pageSize;

    // 当前页数据
    private List<T> records;
}