package com.example.transaction.dto.notice;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Notice;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @ClassName: NoticeInfo
 * @Description: 返回的notice
 * @Author: 曾志昊
 * @Date: 2020/5/14 11:53
 */
@Data
public class NoticeInfo {
    /*notice id*/
    private Integer id;
    /*长度是否超过50*/
    private Boolean showAll;
    /*标题*/
    private String title;
    /*地址*/
    private String address;
    /*第一张商品图片*/
    private String image;


    /*用户头像*/
    private String avatar;
    /*用户名*/
    private String userName;
    /*用户成交率*/
    private Double rate;


    /*发布的时间到今天的距离：一年前 一周前*/
    private String time;


    public NoticeInfo() {
    }

    public NoticeInfo(Notice notice) {
        this.id = notice.getId();
        if (notice.getTitle().length() > 50) {
            this.title = notice.getTitle().substring(0, 49) + "...";
            this.showAll = true;
        } else {
            this.title = notice.getTitle();
            this.showAll = false;
        }
        this.address = notice.getAddress();
        this.image = notice.getCommodityLists().get(0).getCommodityImages().get(0).getImageUrl();
        this.avatar = notice.getUser().getAvatarUrl();
        this.userName = notice.getUser().getUsername();
        this.rate = notice.getUser().getEstimate().getSuccessRate();

        Long deviance = (new Timestamp(System.currentTimeMillis())).getTime() - notice.getCreateTime().getTime();
        if (deviance / (1000 * 60 * 60 * 24 * 365) > 0) {
            this.time = (int) (1000 * 60 * 60 * 24 * 365) + "年前";
        } else if (deviance / (1000 * 60 * 60 * 24 * 30) > 0) {
            this.time = (int) (1000 * 60 * 60 * 24 * 30) + "月前";
        } else if (deviance / (1000 * 60 * 60 * 24 * 7) > 0) {
            this.time = (int) (1000 * 60 * 60 * 24 * 7) + "周前";
        } else if (deviance / (1000 * 60 * 60 * 24) > 0) {
            this.time = (int) (1000 * 60 * 60 * 24) + "天前";
        } else {
            this.time = "今天";
        }

    }

}
