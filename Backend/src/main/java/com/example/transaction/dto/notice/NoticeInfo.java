package com.example.transaction.dto.notice;

import com.example.transaction.pojo.CommodityImage;
import com.example.transaction.pojo.Notice;
import com.example.transaction.util.code.Nums;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
        for (int i = 0; i < notice.getComList().size(); i++) {
            List<CommodityImage> images = notice.getComList().get(i).getCommodityImages();
            if (images == null || images.size() == 0) {
                continue;
            } else {
                this.image = Nums.commodityImagePath + images.get(0).getImageUrl();
            }
        }
        this.avatar = Nums.avatarPath + notice.getUser().getAvatarUrl();
        this.userName = notice.getUser().getUsername();
        if (notice.getUser().getEstimate() != null) {
            this.rate = notice.getUser().getEstimate().getSuccessRate();
        }
        Date now = new Date();
        Long millis = notice.getCreateTime().getTime();
        Long deviance = (new Date()).getTime() - notice.getCreateTime().getTime();

        if (deviance / Nums.year > 0) {
            this.time = (deviance / Nums.year) + "年前";
        } else if (deviance / Nums.month > 0) {
            this.time = (deviance / Nums.month) + "月前";
        } else if (deviance / Nums.weak > 0) {
            this.time = (deviance / Nums.weak) + "周前";
        } else if (deviance / Nums.day > 0) {
            this.time = (deviance / Nums.day) + "天前";
        } else {
            this.time = "今天";
        }
    }

}
