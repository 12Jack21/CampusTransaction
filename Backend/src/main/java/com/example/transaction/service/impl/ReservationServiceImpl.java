package com.example.transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.transaction.dao.A2aDAO;
import com.example.transaction.dao.CommodityDAO;
import com.example.transaction.dao.ReservationDAO;
import com.example.transaction.pojo.A2a;
import com.example.transaction.pojo.AccountNotify;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.MyPage;
import com.example.transaction.util.Nums;
import com.example.transaction.util.code.ReservationCode;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * @ClassName: ReservationServiceImpl
 * @Description: 预约
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:32
 */
@Service("ReservationService")
public class ReservationServiceImpl implements ReservationService {

    /**
     * 建立预约
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer setUpReservation(Reservation reservation) {
        if(reservationDAO.insert(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        /*TODO 将预约信息添加到notify中*/
        AccountNotify accountNotify = new AccountNotify();
        return responseFromServer.success();
    }

    /**
     * 取消预约：state = CANCELED
     * @param reservationId
     * @param accountId
     * @return
     */
    @Override
    @Transactional
    public responseFromServer cancelReservation(Integer reservationId,Integer accountId) {
        Reservation reservation = reservationDAO.selectWithDetailedCommodityById(reservationId);
        if(accountId.intValue()!=reservation.getAccountId().intValue()
                &&accountId.intValue() != reservation.getCommodity().getNotice().getAccountId()){
            /*既不是卖家也不是买家*/
            return responseFromServer.illegal();
        }
        /*身份验证成功*/
        if(reservation.getStateEnum()==ReservationCode.VALIDATE.getCode()){
            /*如果已经validate，则需要恢复库存*/
            Commodity commodity = reservation.getCommodity();
            Commodity newCommodity = new Commodity();
            newCommodity.setId(commodity.getId());
            newCommodity.setCount(commodity.getCount()+reservation.getCount());
            if(commodityDAO.updateById(newCommodity)!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return responseFromServer.error();
            }
        }else if(reservation.getStateEnum()==ReservationCode.FINISHED.getCode()){
            /*如果finish则不可以进行修改取消操作*/
            return responseFromServer.illegal("不可取消已完成的订单");
        }

        reservation.setStateEnum(ReservationCode.CANCELLED.getCode());
        if(reservationDAO.updateById(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error("修改状态错误");
        }
        return responseFromServer.success();
    }

    /**
     * 删除预约信息
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer deleteReservation(Reservation reservation) {
        if(reservationDAO.deleteById(reservation.getId())!=1){
            /*手动回滚事务*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }else{
            return responseFromServer.success();
        }
    }

    /**
     * 查询预约信息分页
     * @param queryWrapper
     * @param pageIndex
     * @return
     */
    @Override
    public responseFromServer getReservationsPage(QueryWrapper queryWrapper,Integer pageIndex) {
        Page<Reservation> page = new Page<>(pageIndex, Nums.pageSize);
        IPage<Reservation> reservationPage = reservationDAO.selectPage(page,queryWrapper);
        MyPage myPage = new MyPage(reservationPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 获取向我的预约请求
     * @param accountId
     * @param pageIndex
     * @return
     */
    @Override
    public responseFromServer getReservationRequest(Integer accountId,Integer pageIndex) {
        Page<Reservation> page = new Page<>(pageIndex,Nums.pageSize);
        IPage<Reservation> reservationPage = reservationDAO.getReservationRequestPage(page,accountId);
        MyPage myPage = new MyPage(reservationPage);
        return responseFromServer.success(myPage);
    }

    /**
     * 更新预约
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer updateReservation(Reservation reservation) {
        if(reservationDAO.updateById(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 更新预约
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer updateBuyerReservation(Reservation reservation) {
        Reservation oldReservation = reservationDAO.selectById(reservation.getId());
        if(oldReservation.getStateEnum() != ReservationCode.WAITING.getCode()){
            return responseFromServer.illegal();
        }
        if(reservationDAO.updateById(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return  responseFromServer.error();
        }
        return responseFromServer.success();
    }



    /**
     * 设置预约成功
     * @param reservation
     * @return
     */
    @Override
    @Transactional
    public responseFromServer validateReservation(Reservation reservation,Integer sellerId) {
        /*用户验证在controller层中处理*/
        /*获取reservation 检查id和用户id*/
        if(reservation.getCommodityId()==null)return responseFromServer.error();
        /*验证reservation状态是否是等待状态*/
        if(reservation.getStateEnum()!=ReservationCode.WAITING.getCode()){
            return responseFromServer.error("预约状态错误");
        }
        /*验证状态成功*/

        /*验证commodity库存*/
        Commodity commodity = commodityDAO.selectById(reservation.getCommodityId());
        if(commodity.getCount()<reservation.getCount()){
            /*库存不足*/
            return responseFromServer.error("库存不足");
        }
        /*库存充足*/

        /*修改reservation状态*/
        reservation.setStateEnum(ReservationCode.VALIDATE.getCode());
        if(reservationDAO.updateById(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error("修改预约状态错误");
        }
        Commodity newCommodity = new Commodity();
        newCommodity.setCount(commodity.getCount()-reservation.getCount());
        newCommodity.setId(commodity.getId());
        if(commodityDAO.updateById(newCommodity)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error("修改商品库存出错");
        }


        reservation = reservationDAO.selectById(reservation.getId());

            A2a a2a1 = new A2a(reservation.getAccountId(),sellerId);
            A2a a2a2 = new A2a(sellerId,reservation.getAccountId());
            if(a2aDAO.insert(a2a1)!=1||a2aDAO.insert(a2a2)!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }
        return responseFromServer.success();
    }

    /**
     * 设置订单完成
     * @param reservationId
     * @return
     */
    @Override
    @Transactional
    public responseFromServer finishReservation(Integer reservationId) {
        Reservation reservation = reservationDAO.selectWithDetailedCommodityById(reservationId);
        reservation.setStateEnum(ReservationCode.FINISHED.getCode());
        if(reservationDAO.updateById(reservation)!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return responseFromServer.error();
        }else{
            return responseFromServer.success();
        }
    }


    /**
     * 查询详细预约信息（包含商品信息）
     * @param reservationId
     * @return
     */
    public responseFromServer getDetailedReservation(Integer reservationId){
        Reservation reservation = reservationDAO.selectWithDetailedCommodityById(reservationId);
        if(reservation == null)return responseFromServer.error();
        else return responseFromServer.success(reservation);
    }


    /**
     * 查询简单预约信息
     * @param reservationId
     * @return
     */
    public responseFromServer getSimpleReservation(Integer reservationId){
        Reservation reservation = reservationDAO.selectById(reservationId);
        if(reservation == null) return responseFromServer.error();
        else return responseFromServer.success(reservation);
    }


    A2aDAO a2aDAO;
    ReservationDAO reservationDAO;
    CommodityDAO commodityDAO;
    @Autowired
    public ReservationServiceImpl(ReservationDAO reservationDAO,CommodityDAO commodityDAO,A2aDAO a2aDAO){
        this.reservationDAO = reservationDAO;
        this.commodityDAO = commodityDAO;
        this.a2aDAO = a2aDAO;
    }


}
