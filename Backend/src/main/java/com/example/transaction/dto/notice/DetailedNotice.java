package com.example.transaction.dto.notice;

import com.example.transaction.dto.commodity.CommodityInfo;
import com.example.transaction.dto.commodity.SimpleCommodity2;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.CommodityImage;
import com.example.transaction.pojo.Notice;
import com.example.transaction.util.PathUtil;
import com.example.transaction.util.code.NoticeCode;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.code.ResourcePath;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: NoticeInfo
 * @Description: 返回的notice
 * @Author: 曾志昊
 * @Date: 2020/5/14 11:53
 */
@Data
public class DetailedNotice {
    /*notice id*/
    private Integer id;
    /*标题*/
    private String title;

    private String conditions = "";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-Mm-dd HH:mm")
    private Date expiredTime;

    private String description = "";
    private String address = "";
    private String detailedAddress = "";
    private String userName = "";
    private Double rate = 1.0D;
    private String avatar = "";
    private Integer stateEnum;
    private String stateEnumStr = "";
    public void setStateEnum(Integer stateEnum){
        if (stateEnum != null) {
            this.stateEnum = stateEnum;
            this.stateEnumStr = NoticeCode.getDescription(stateEnum);
        }else{
            this.stateEnum = null;
            this.stateEnumStr = "";
        }

    }
    private Integer browseCount = -1;
    private Integer accountId = -1;
    List<SimpleCommodity2> commodityList;

    public DetailedNotice() {
    }

    public DetailedNotice(Notice notice) {
        this.id = notice.getId();
        this.address = notice.getAddress();
        this.detailedAddress = notice.getDetailedAddress();
        this.commodityList = new ArrayList<>();
        this.title = notice.getTitle();
        for (int i = 0; i < notice.getComList().size(); i++) {
            Commodity commodity = notice.getComList().get(i);
            this.commodityList.add(new SimpleCommodity2(commodity));
        }
        this.browseCount = notice.getBrowseCount();
        this.avatar = (PathUtil.isPath(notice.getUser().getAvatar())?"":ResourcePath.avatarRequestPath)+notice.getUser().getAvatar();
        this.userName = notice.getUser().getUsername();
        this.accountId = notice.getAccountId();
        if (notice.getUser().getEstimate() != null) {
            this.rate = notice.getUser().getEstimate().getSuccessRate();
        }
        this.description = notice.getDescription();
        this.conditions = notice.getConditions();
        this.expiredTime = notice.getEndTime();
        this.setStateEnum(notice.getStateEnum());
    }
}
