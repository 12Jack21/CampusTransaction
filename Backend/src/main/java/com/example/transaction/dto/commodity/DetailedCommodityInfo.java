package com.example.transaction.dto.commodity;

import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.comment.SimpleComment;
import com.example.transaction.pojo.Comment;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.pojo.base.BaseCommodity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: DetailedCommodityInfo
 * @Description: 商品详情页的商品信息, 包含商品信息以及买家信息和评论
 * @Author: 曾志昊
 * @Date: 2020/5/24 15:09
 */
@Data
public class DetailedCommodityInfo {

    BaseCommodity commodity;
    SimpleAccount account;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    Date expiredTime;
    String conditions = "";
    /**
     * 通告的地址
     */
    String address = "";

    List<SimpleComment> comments;

    Pagination pagination;

    public DetailedCommodityInfo() {
        pagination = new Pagination();
        pagination.setPageIndex(1);
    }

    public DetailedCommodityInfo(Commodity commodity) {
        if (commodity != null) {
            this.commodity = commodity;
            if (commodity.getNotice() != null) {
                Notice notice = commodity.getNotice();
                this.expiredTime = notice.getEndTime();
                this.conditions = notice.getCondition();
                this.address = notice.getAddress();
                this.account = new SimpleAccount(notice.getUser());
            }
        }
    }

}
