package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.NoticeDAO;
import com.example.transaction.pojo.Notice;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.NoticeService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;

/**
 * @ClassName: NoticeServiceImpl
 * @Description: TODO
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
        IPage<Notice> noticeIPage = noticeDAO.selectPage(page,queryWrapper);
        MyPage myPage = new MyPage(noticeIPage);
        return responseFromServer.success(myPage);
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

    @Autowired
    public NoticeServiceImpl(NoticeDAO noticeDAO){
        this.noticeDAO = noticeDAO;
    }
}
