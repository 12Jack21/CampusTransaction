package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.pojo.Notice;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.code.Nums;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @ClassName: NoticeServiceImpl
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:46
 */
@Service(value = "noticeService")
public class NoticeServiceImpl implements NoticeService {

    /**
     * 建立一个通告
     * @param notice
     * @return
     */
    @Transactional
    @Override
    public responseFromServer setupNotice(Notice notice) {
        /*此时默认notice中各项数据正常*/
        /*添加商品*/
        notice.setId(null);
        if(noticeDAO.insert(notice)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }else{
            /*创建notice成功*/
            Integer noticeId = notice.getId();
            return responseFromServer.success(notice);
        }

    }


    /**
     * 删除特定的notice
     * @param queryWrapper
     * @return
     */
    @Override
    @Transactional
    public responseFromServer deleteNotice(QueryWrapper queryWrapper) {
//        if(commodityService.deleteAllByNoticeId(notice))
        Notice notice = noticeDAO.getNoticeWithAllCommodity(queryWrapper);
        if(notice == null)return responseFromServer.error();
        commodityService.deleteAllByNotice(notice);
        if(noticeDAO.delete(queryWrapper)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }


    /**
     * 取消通告
     * @param notice
     * @param queryWrapper
     * @return
     */
    @Override
    @Transactional
    public responseFromServer updateNotice(Notice notice, QueryWrapper queryWrapper) {

        if(noticeDAO.update(notice,queryWrapper)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }else{
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
    public responseFromServer getNoticePage(QueryWrapper queryWrapper, int pageIndex){
        Page<Notice> page = new Page<>(pageIndex,Nums.pageSize);
        IPage<Notice> noticeIPage = noticeDAO.getDetailedNoticePage(page,queryWrapper);
        MyPage myPage = new MyPage(noticeIPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 获取通告信息
     * @param noticeId
     * @return
     */
    @Override
    public responseFromServer getSimpleNotice(Integer noticeId) {
        Notice notice = noticeDAO.selectById(noticeId);
        if(notice == null)return responseFromServer.error();
        return responseFromServer.success(notice);
    }

    /**
     * 获得详细通告内容，包括商品信息
     * @param noticeId
     * @return
     */
    @Override
    public responseFromServer getDetailedNotice(Integer noticeId) {
        Notice notice = noticeDAO.getNoticeWithAllCommodityById(noticeId);
        if(notice == null)return responseFromServer.error();
        return responseFromServer.success(notice);
    }


    /*
    @Override
    public responseFromServer getRecentNoticePage(Map<String, Object> map) {

        return null;
    }

    @Override
    public responseFromServer getNoticePageByAccountId(Map<String, Object> map) {
        return null;
    }
*/

    NoticeDAO noticeDAO;
    CommodityDAO commodityDAO;
    CommodityService commodityService;

    @Autowired
    public NoticeServiceImpl(NoticeDAO noticeDAO,CommodityDAO commodityDAO, CommodityService commodityService){
        this.noticeDAO = noticeDAO;
        this.commodityDAO = commodityDAO;
        this.commodityService = commodityService;
    }
}
