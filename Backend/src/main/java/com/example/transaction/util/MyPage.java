package com.example.transaction.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: MyPage
 * @Description: 自定义分页函数
 * @Author: 曾志昊
 * @Date: 2020/4/27 15:55
 */
@Data
public class MyPage<T> {
    /*总记录数*/
    private long totalCount;
    /*当前分页位置*/
    private long pageIndex;
    /*总分页个数*/
    private long pageCount;
    /*分页大小*/
    private long pageSize;
    /*数据列表*/
    private List<T> pageList;

    public MyPage(Integer totalCount, Integer pageIndex, Integer pageCount) {
        this.totalCount = totalCount;
        this.pageIndex = pageIndex;
        this.pageCount = pageCount;
    }

    public MyPage(IPage page){
        this.totalCount = page.getTotal();
        this.pageIndex = page.getCurrent();
        this.pageCount = page.getPages();
        this.pageList = page.getRecords();
        this.pageSize = pageList.size();
    }
}
