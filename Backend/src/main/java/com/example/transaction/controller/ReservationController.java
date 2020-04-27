package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.responseFromServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @ClassName: ReservationController
 * @Description: TODO
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:30
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    /**
     * 创建预约
     * @param reservation
     * @param session
     * @return
     */
    @RequestMapping("/setupReservation")
    public responseFromServer setupReservation(@RequestBody  Reservation reservation, HttpSession session){
        Account account = (Account) session.getAttribute("currentAccount");
        if(reservation.getCommodityId()==null||reservation.getCount()==null){
            return responseFromServer.error();
        }else{
            reservation.setAccountId(account.getId());
            return reservationService.setUpReservation(reservation);
        }
    }

    /**
     * 取消预约
     * @param reservation
     * @param session
     * @return
     */
    @RequestMapping("/cancelReservation")
    public responseFromServer cancelReservation(@RequestBody Reservation reservation,HttpSession session){
        Account account = (Account)session.getAttribute("currentAccount");
        if(AccountVerify.verify(account,session)){
            return reservationService.cancelReservation(reservation);
        }else{
            return responseFromServer.illegal();
        }
    }


    /**
     * 删除预约
     * @param reservation
     * @param session
     * @return
     */
    @RequestMapping("/deleteReservation")
    public responseFromServer deleteReservation(@RequestBody Reservation reservation,HttpSession session){
        Account account = (Account)session.getAttribute("currentAccount");
        if(AccountVerify.verify(account,session)){
            return reservationService.deleteReservation(reservation);
        }else{
            return responseFromServer.illegal();
        }
    }


    /**
     *
     * @param reservation
     * @param session
     * @return
     */
    @RequestMapping("/validateReservation")
    public responseFromServer validateReservation(@RequestBody Reservation reservation, HttpSession session){
        /*TODO*/
        /*检查*/
        /*修改reservation状态*/
        /*修改commodity库存*/
        return null;
    }


    /*查看商品的预约*/

    /*查看我申请的预约*/

    /*查看我接收到的预约*/



    @RequestMapping("/getReservationPage")
    public responseFromServer getReservationPage(){
        /*TODO*/
        return null;
    }
}
