package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.dto.notice.NoticeCondition;
import com.example.transaction.dto.notice.NoticeInfo;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.code.NoticeCode;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: NoticeServiceImpl
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:46
 */
@Service(value = "noticeService")
public class NoticeServiceImpl implements NoticeService {



    @Override
    public responseFromServer getRecentNotice(NoticeCondition condition) {
        if (condition.getPageIndex() == null || condition.getPageIndex() <= 0) {
            condition.setPageIndex(1);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        /*按照时间倒序排序*/
        queryWrapper.orderByDesc("create_time");
        /*只查看确认发布的通告*/
        queryWrapper.eq("state_enum", NoticeCode.PUBLISHED.getCode());
        /*是商品还是需求*/
        switch (condition.getType()) {
            case 0:/*全部*/
                break;
            case 1:/*出售*/
                queryWrapper.eq("type", true);
                break;
            case 2:/*需求*/
                queryWrapper.eq("type", false);
                break;
        }
        if (condition.getAccountId() != null) {
            queryWrapper.eq("account_id", condition.getAccountId());
        }
        if (condition.getEndTime() != null) {
            queryWrapper.le("create_time", (new Timestamp(condition.getEndTime().getTime())));
        }
        return getNoticePage(queryWrapper, condition.getPageIndex());
    }

    /**
     * 删除特定的notice
     *
     * @param queryWrapper
     * @return
     */
    @Override
    @Transactional
    public responseFromServer deleteNotice(QueryWrapper queryWrapper) {
//        if(commodityService.deleteAllByNoticeId(notice))
        Notice notice = noticeDAO.getNoticeWithAllCommodity(queryWrapper);
        if (notice == null) {
            return responseFromServer.error();
        }
        commodityService.deleteAllByNotice(notice);
        if (noticeDAO.delete(queryWrapper) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }


    /**
     * 建立一个不包含商品的通告
     *
     * @param notice
     * @return
     */
    @Transactional
    @Override
    public responseFromServer setupNotice(Notice notice) {
        /*此时默认notice中各项数据正常*/
        /*添加商品*/
        notice.setId(null);
        if (noticeDAO.insert(notice) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        } else {
            /*创建notice成功*/
            Integer noticeId = notice.getId();
            return responseFromServer.success(notice);
        }
    }

    /**
     * 添加通告(包含商品信息)
     *
     * @param notice
     * @return
     */
    @Override
    @Transactional
    public responseFromServer addNotice(Notice notice) {
        if (notice == null || notice.getComList() == null || notice.getComList().size() == 0) {
            return responseFromServer.error();
        }
        if (noticeDAO.insert(notice) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        } else {
            List<Commodity> commodityList = notice.getComList();
            for (Commodity commodity : commodityList) {
                commodity.setNoticeId(notice.getId());
                if (commodityService.insertCommodity(commodity).isFailure()) {
                    /*函数中包含了对类型还有商品图像的处理*/
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return responseFromServer.error();
                }
            }
            return responseFromServer.success();
        }
    }


    /**
     * 取消通告
     *
     * @param notice
     * @param queryWrapper
     * @return
     */
    @Override
    @Transactional
    public responseFromServer updateNotice(Notice notice, QueryWrapper queryWrapper) {
        if (noticeDAO.update(notice, queryWrapper) != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        } else {
            return responseFromServer.success();
        }
    }

    /**
     * 查询通告分页
     * @param queryWrapper
     * @param pageIndex
     * @return
     */
    @Override
    public responseFromServer getNoticePage(QueryWrapper queryWrapper, int pageIndex) {
        Page<Notice> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Notice> noticeIPage = noticeDAO.getDetailedNoticePage(page, queryWrapper);
        MyPage<Notice> myPage = new MyPage(noticeIPage);
        List<NoticeInfo> noticeList = new ArrayList<>();
        for (Notice notice : myPage.getPageList()) {
            noticeList.add(new NoticeInfo(notice));
        }
        return responseFromServer.success(new MyPage<>(myPage, noticeList));
    }

    /**
     * 获取通告信息
     *
     * @param noticeId
     * @return
     */
    @Override
    public responseFromServer getSimpleNotice(Integer noticeId) {
        Notice notice = noticeDAO.selectById(noticeId);
        if (notice == null) {
            return responseFromServer.error();
        }
        return responseFromServer.success(notice);
    }

    /**
     * 获得详细通告内容，包括商品信息
     *
     * @param noticeId
     * @return
     */
    @Override
    public responseFromServer getDetailedNotice(Integer noticeId) {
        Notice notice = noticeDAO.getNoticeWithAllCommodityById(noticeId);
        if (notice == null) {
            return responseFromServer.error();
        }
        return responseFromServer.success(notice);
    }


    NoticeDAO noticeDAO;
    CommodityDAO commodityDAO;
    CommodityService commodityService;

    @Autowired
    public NoticeServiceImpl(NoticeDAO noticeDAO, CommodityDAO commodityDAO, CommodityService commodityService) {
        this.noticeDAO = noticeDAO;
        this.commodityDAO = commodityDAO;
        this.commodityService = commodityService;
    }
}
