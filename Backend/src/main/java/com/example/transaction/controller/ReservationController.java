package com.example.transaction.controller;

import com.example.transaction.pojo.Account;
import com.example.transaction.pojo.Commodity;
import com.example.transaction.pojo.Reservation;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.AccountVerify;
import com.example.transaction.util.code.ReservationCode;
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
            reservation.setStateEnum(ReservationCode.WAITING.getCode());
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
        Account account = (Account) session.getAttribute("currentAccount");
        if(reservation.getId()!=null)
            return reservationService.cancelReservation(reservation.getId(),account.getId());
        else
            return responseFromServer.error();
    }


    /**
     * 删除预约
     * @param reservation
     * @param session
     * @return
     */
    @RequestMapping("/deleteReservation")
    public responseFromServer deleteReservation(@RequestBody Reservation reservation,HttpSession session){
        Account account = new Account(reservation.getId());
        if(AccountVerify.verify(account,session)){
            return reservationService.deleteReservation(reservation);
        }else{
            return responseFromServer.illegal();
        }
    }


    /**
     * 设置预约成功----减少库存
     * @param reservation
     * @param session
     * @return
     */
    @RequestMapping("/validateReservation")
    public responseFromServer validateReservation(@RequestBody Reservation reservation, HttpSession session){
        Account account = (Account) session.getAttribute("currentAccount");
        if(reservation.getId()==null)
            return responseFromServer.error();
        else{
//            Reservation reservation = reservationDAO.selectWithDetailedCommodityById(reservationId);
            responseFromServer response = reservationService.getSimpleReservation(reservation.getId());
            if(response.isSuccess()){
                /*用户身份验证*/
                reservation = (Reservation) response.getData();
                Commodity commodity = reservation.getCommodity();
                if(commodity !=null
                        &&commodity.getNotice()!=null
                        &&commodity.getNotice().getAccountId()!=null){
                    if(commodity.getNotice().getAccountId().intValue() != account.getId().intValue())
                        /*此时要操作的用户跟notice的卖家不符合 非法操作*/
                        return responseFromServer.illegal();
                }else{
                    /*查询错误*/
                    return responseFromServer.error("查询错误");
                }
                /*用户验证成功*/
                return reservationService.validateReservation(reservation.getId(),account);
            }else{
                return responseFromServer.error();
            }
        }
    }



    @RequestMapping("/finishReservation")
    public responseFromServer finishBuyerReservation(@RequestBody Reservation reservation,HttpSession session){
        Account account = new Account(reservation.getId());
        /*判断当前预约是validate的*/
        return null;
    }

    @RequestMapping("/finishSellerReservation")
    public responseFromServer finishSellerReservation(@RequestBody Reservation reservation,HttpSession session){
        /*判断当前用户是卖家*/
        /*判断当前预约的状态*/
        return null;
    }

    /*查看商品的预约*/
    @RequestMapping("/getReservationPageForCommodity")
    public responseFromServer getReservationPageForCommodity(@RequestBody Commodity commodity, HttpSession session){
        /*用户核对*/
        return null;
    }

    /*查看我申请的预约*/
    @RequestMapping("/getMyReservation")
    public responseFromServer getMyReservation(HttpSession session){
        return null;
    }

    /*todo 需要注意的问题是返回的reservation包含数据的问题
    *  是否要包含commodity对象？dao编写？*/

    /*查看我接收到的预约*/
    @RequestMapping("/getReservationRequest")
    public responseFromServer getReservationRequest(HttpSession session){
        return null;
    }


    @RequestMapping("/getReservationPage")
    public responseFromServer getReservationPage(){
        /*TODO*/
        return null;
    }
}
