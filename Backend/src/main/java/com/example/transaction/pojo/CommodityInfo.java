package com.example.transaction.pojo;

import com.example.transaction.dto.notice.NoticeInfo;
import com.example.transaction.util.code.Nums;
import io.netty.util.internal.StringUtil;
import lombok.Data;

/**
 * @ClassName: CommodityInfo
 * @Description: 用于返回的商品信息
 * @Author: 曾志昊
 * @Date: 2020/5/21 20:00
 */
@Data
public class CommodityInfo {
    Integer id;
    Integer count;
    Double originalPrice;
    Double expectedPrice;
    String state;
    String time;
    String name;
    String img = "";
    String accountName;
    String avatar = "";

    public CommodityInfo(Commodity commodity, NoticeInfo noticeInfo) {
        if (commodity != null) {
            this.id = commodity.getId();
            this.count = commodity.getCount();
            this.originalPrice = commodity.getOriginalPrice();
            this.expectedPrice = commodity.getExpectedPrice();
            this.name = commodity.getName();
            if(commodity.getCommodityImages()!=null&&!commodity.getCommodityImages().isEmpty()){
                this.img = Nums.commodityImagePath + commodity.getCommodityImages().get(0).getImageUrl();
            }
//            if (!StringUtil.isNullOrEmpty(noticeInfo.getImage())) {
//                this.img = Nums.commodityImagePath + noticeInfo.getImage();
//            }

            if (noticeInfo == null) {
                /**
                 * ZZH
                 * TODO : ????????
                 */
            }
            /**
             * ZZH
             * TODO : 发布中 已预约
             */
            this.state = "";
            this.time = noticeInfo.getTime();
            this.accountName = noticeInfo.getUserName();
            if (!StringUtil.isNullOrEmpty( noticeInfo.getAvatar())) {
                this.avatar = Nums.avatarPath +  noticeInfo.getAvatar();
            }
        }

    }


}
