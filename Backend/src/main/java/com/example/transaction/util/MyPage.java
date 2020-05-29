package com.example.transaction.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: MyPage
 * @Description: 自定义分页函数
 * @Author: 曾志昊
 * @Date: 2020/4/27 15:55
 */
@Data
public class MyPage<T> {
    /**
     * 总记录数
     */
    private Integer totalCount = -1;
    /**
     * 当前分页位置
     */
    private Integer pageIndex = -1;
    /**
     * 总分页个数
     */
    private Integer pageCount = -1;
    /**
     * 分页大小
     */
    private Integer pageSize = -1;
    /**
     * 数据列表
     */
    private List<T> pageList;
    /**
     * 查询的截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date endTime = null;

    public MyPage(IPage<T> page) {
        this.totalCount = (int) page.getTotal();
        this.pageIndex = (int)page.getCurrent()+1;
//        this.pageIndex = (int)Math.ceil((double) page.getCurrent()/ Nums.pageSize);
        this.pageCount = (int) page.getPages();
        this.pageList = page.getRecords();
        this.pageSize = pageList.size();
    }


    public MyPage(MyPage page, List list) {
        this.totalCount = page.totalCount;
        this.pageIndex = page.pageIndex+1;
        this.pageCount = page.pageCount;
        this.pageList = list;
        this.pageSize = pageList.size();
    }
}
