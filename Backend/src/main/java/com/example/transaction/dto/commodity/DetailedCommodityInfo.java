package com.example.transaction.dto.commodity;

import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.dto.comment.SimpleComment;
import com.example.transaction.pojo.Comment;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.pojo.base.BaseCommodity;
import com.example.transaction.util.code.NoticeCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    Date expiredTime;
    String conditions = "";
    Integer stateEnum = -1;
    private String stateEnumStr;
    public void setStateEnum(Integer stateEnum){
        this.stateEnum = stateEnum;
        this.stateEnumStr = NoticeCode.getDescription(stateEnum);
    }

    /**
     * 通告的地址
     */
    String address = "";
    String detailedAddress = "";

//    Pagination pagination;

    List<SimpleComment> comments;
//    public DetailedCommodityInfo() {
//        pagination = new Pagination();
//        pagination.setPageIndex(1);
//    }

    public DetailedCommodityInfo(Commodity commodity) {
        if (commodity != null) {
            this.commodity = commodity;
            if (commodity.getNotice() != null) {
                Notice notice = commodity.getNotice();
                this.expiredTime = notice.getEndTime();
                this.conditions = notice.getConditions();
                this.address = notice.getAddress();
                this.detailedAddress = notice.getDetailedAddress()==null?"":notice.getDetailedAddress();
                this.setStateEnum(notice.getStateEnum());
                this.account = new SimpleAccount(notice.getUser());
            }
            commodity.setNotice(null);
        }
    }

    public void setCommentsFromCommentList(List<Comment> comments) {
        if (comments != null) {
            this.comments = new ArrayList<>();
            for (Comment comment : comments) {
                this.comments.add(new SimpleComment(comment));
            }
        }
    }

    public void setComments(List<SimpleComment> comments){
        this.comments = comments;
    }

}
