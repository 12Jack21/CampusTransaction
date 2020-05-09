package com.example.transaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.pojo.*;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.code.ReservationCode;
import com.example.transaction.util.responseFromServer;
import com.example.transaction.service.impl.AccountVerify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName: ReservationController
 * @Author: 曾志昊
 * @Date: 2020/4/26 16:30
 */
@RestController
@RequestMapping("/reservations")
@Api(tags = "ReservationController")
public class ReservationController {

    @Autowired
    ReservationService reservationService;
    @Autowired
    NoticeService noticeService;
    @Autowired
    CommodityService commodityService;
    @Autowired
    AccountVerify accountVerify;

    /**
     * 创建预约
     *
     * @param reservation
     * @param request
     * @return
     */
//    @RequestMapping("/setupReservation")
    @ApiOperation(value = "创建预约")
    @ApiImplicitParam(name = "reservation", value = "@RequestBody 预约请求体",  paramType = "Reservation", dataType = "Reservation")
    @PostMapping
    public responseFromServer setupReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
//        Account account = (Account) request./* 修改获取账号方式*/getAttribute("currentAccount");
        Account account = accountVerify.getCurrentAccount(request);
        if (reservation.getCommodityId() == null || reservation.getCount() == null) {
            return responseFromServer.error();
        } else {
            reservation.setAccountId(account.getId());
            reservation.setStateEnum(ReservationCode.WAITING.getCode());
            return reservationService.setUpReservation(reservation);
        }
    }

    /**
     * 取消预约
     *
     * @param reservation
     * @param request
     * @return
     */
//    @RequestMapping("/cancelReservation")
    @ApiOperation(value = "取消预约")
    @ApiImplicitParam(name = "reservation_id", value = "reservation_id",  paramType = "Integer", dataType = "Integer")
    @PutMapping("/{reservation_id}/cancel")
    public responseFromServer cancelReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
//        Account account = (Account) request./* 修改获取账号方式*/getAttribute("currentAccount");
        Account account = accountVerify.getCurrentAccount(request);

        if (reservation.getId() != null)
            return reservationService.cancelReservation(reservation.getId(), account.getId());
        else
            return responseFromServer.error();
    }


    /**
     * 删除预约
     *
     * @param reservation
     * @param request
     * @return
     */
//    @RequestMapping("/deleteReservation")
    @ApiOperation(value = "删除预约")
    @ApiImplicitParam(name = "reservation_id", value = "预约Id",  paramType = "Integer", dataType = "Integer")
    @DeleteMapping("/{reservation_id}")
>>>>>>> 6e3c06f0210a1e588e7d6b0e5c099141f4eac44d
    public responseFromServer deleteReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
        if (verifySeller(reservation, request)) {
            /*在验证的时候已经更新reservation信息*/
            return reservationService.deleteReservation(reservation);
        } else {
            return responseFromServer.illegal();
        }
    }


    /**
     * 通过reservationId 验证请求用户是否是对应的卖家
     *
     * @param reservation
     * @param request
     * @return
     */
    @ApiOperation(value = "用户身份验证")
    @ApiImplicitParam(name = "reservation_id", value = "预约Id",  paramType = "Integer", dataType = "Integer")
    @GetMapping("/{reservation_id}/verify")
    public boolean verifySeller(Reservation reservation, HttpServletRequest request) {
//        Account account = (Account) request./* 修改获取账号方式*/getAttribute("currentAccount");
        Account account = accountVerify.getCurrentAccount(request);
        if (reservation.getId() == null)
            return false;
        else {
            responseFromServer response = reservationService.getDetailedReservation(reservation.getId());
            if (response.isSuccess()) {
                /*用户身份验证*/
                reservation = (Reservation) response.getData();
                Commodity commodity = reservation.getCommodity();
                if (commodity != null
                        && commodity.getNotice() != null
                        && commodity.getNotice().getAccountId() != null) {
                    if (commodity.getNotice().getAccountId().intValue() != account.getId().intValue())
                        /*此时要操作的用户跟notice的卖家不符合 非法操作*/
                        return false;
                } else {
                    /*查询错误*/
                    return false;
                }
                /*用户验证成功*/
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * 设置预约成功----减少库存
     *
     * @param reservation
     * @param request
     * @return
     */

//    @RequestMapping("/validateReservation")
    @ApiOperation(value = "预约成功，减小库存")
    @ApiImplicitParam(name = "reservation_id", value = "预约Id",  paramType = "Integer", dataType = "Integer")
    @PutMapping("/{reservation_id}/validate")
    public responseFromServer validateReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
        if (verifySeller(reservation, request)) {
            /*验证当前操作用户是否是卖家*/
//            return reservationService.validateReservation(reservation, ((Account) request./*  修改获取账号方式*/getAttribute("currentAccount")).getId());
            return reservationService.validateReservation(reservation, accountVerify.getCurrentAccount(request).getId());
        } else {
            return responseFromServer.illegal();
        }
    }


    /**
     * 卖家设置预约为完成
     *
     * @param reservation
     * @param request
     * @return
     */
//    @RequestMapping("/finishReservation")
    @ApiOperation("预约完成")
    @ApiImplicitParam(name = "reservation_id", value = "预约Id",  paramType = "Integer", dataType = "Integer")
    @PutMapping("/{reservation_id}/finish")
    public responseFromServer finishReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
        /*验证当前操作用户是否是卖家*/
        if (verifySeller(reservation, request)) {
            return reservationService.finishReservation(reservation.getId());
        } else {
            return responseFromServer.illegal();
        }
    }

    /**
     * 查看当前商品的所有预约
     *
     * @param map
     * @param request
     * @return
     */
//    @RequestMapping("/getReservationPageForCommodity")
    @ApiOperation(value = "查看当前商品的所有预约")
    @GetMapping("/commodity/{commodity_id}/receive")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "commodity_id", value = "商品Id",  paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "page_index", value = "页面索引",  paramType = "Integer", dataType = "Integer")
            }
    )
    public responseFromServer getReservationPageForCommodity(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        Commodity commodity = (Commodity) map.get("commodity");
        Integer pageIndex = (Integer) map.get("pageIndex");
        if (commodity == null) return responseFromServer.error();
        pageIndex = pageIndex == null || pageIndex.intValue() <= 0 ? 1 : pageIndex;
        /*用户核对*/
        responseFromServer response = commodityService.getSimpleCommodity(commodity.getId());
        if (!response.isSuccess()) {
            return responseFromServer.error();
        }
        commodity = (Commodity) response.getData();
        Notice notice = (Notice) noticeService.getSimpleNotice(commodity.getNoticeId()).getData();
        if (notice.getAccountId().intValue() == accountVerify.getCurrentAccount(request).getId().intValue()) {
            /*验证成功*/
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("commodity_id", commodity.getId());
            return reservationService.getReservationsPage(queryWrapper, pageIndex);
        } else {
            return responseFromServer.illegal();
        }
    }

    /**
     * 查看我申请的预约
     *
     * @param map
     * @param request
     * @return
     */
//    @RequestMapping("/getMyReservation")
    @ApiOperation(value = "申请预约列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "account_id", value = "用户Id",  paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "page_index", value = "页面索引",  paramType = "Integer", dataType = "Integer")
            }
    )
    @GetMapping("/account/{account_id}/send")
    public responseFromServer getMyReservation(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        Account account = accountVerify.getCurrentAccount(request);
        Integer pageIndex = (Integer) map.get("pageIndex");
        pageIndex = pageIndex == null || pageIndex.intValue() <= 0 ? 1 : pageIndex;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id", account.getId());
        Boolean isCommodity = (Boolean) map.get("isCommodity");
        if (isCommodity != null) {
            queryWrapper.eq("type", isCommodity);
        }
        return reservationService.getReservationsPage(queryWrapper, pageIndex);
    }


    /**
     * 查看我接收到的预约
     *
     * @param map
     * @param request
     * @return
     */
//    @RequestMapping("/getReservationRequest")
    @ApiOperation(value = "接收预约列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "account_id", value = "用户Id",  paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "page_index", value = "页面索引",  paramType = "Integer", dataType = "Integer")
            }
    )
    @GetMapping("/account/{account_id}/receive")
    public responseFromServer getReservationRequest(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        Integer pageIndex = (Integer) map.get("pageIndex");
        pageIndex = pageIndex == null || pageIndex.intValue() <= 0 ? 1 : pageIndex;
        return reservationService.getReservationRequest(accountVerify.getCurrentAccount(request).getId(), pageIndex);
    }

    /**
     * 获取详细的预约内容
     *
     * @param reservation
     * @param request
     * @return
     */
//    @RequestMapping("/getDetailedReservation")
    @ApiOperation("获取详细预约内容")
    @ApiImplicitParam(name = "reservation_id", value = "预约Id",  paramType = "Integer", dataType = "Integer")
    @GetMapping("/{reservation_id}")
    public responseFromServer getDetailedReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
        if (verifySeller(reservation, request)) {
            return responseFromServer.success(reservation);
        } else {
            return responseFromServer.error();
        }
    }

    /**
     * 获取简单预约内容
     *
     * @param reservation
     * @param request
     * @return
     */
//    @RequestMapping("/getSimpleReservation")
    @ApiOperation(value = "获取简单预约内容")
    @ApiImplicitParam(name = "reservation_id", value = "预约Id",  paramType = "Integer", dataType = "Integer")
    @GetMapping("/{reservation_id}/simple")
    public responseFromServer getSimpleReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
        if (verifySeller(reservation, request)) {
            reservation.setUser(null);
            reservation.setCommodity(null);
            return responseFromServer.success(reservation);
        } else {
            return responseFromServer.error();
        }
    }

    /**
     * 更新预约内容
     *
     * @param reservation
     * @param request
     * @return
     */
//    @RequestMapping("/updateReservation")
    @ApiOperation(value = "更新预约内容")
    @ApiImplicitParam(name = "reservation_id", value = "预约Id",  paramType = "Integer", dataType = "Integer")
    @PutMapping("/{reservation_id}")
    public responseFromServer updateBuyerReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
        if (reservation.getStateEnum() != null && reservation.getStateEnum() != ReservationCode.WAITING.getCode()) {
            return responseFromServer.illegal();
        }
        Account account = accountVerify.getCurrentAccount(request);
        Reservation reservation1 = (Reservation) reservationService.getSimpleReservation(reservation.getId()).getData();
        if (reservation1 == null || reservation1.getAccountId() == null) {
            return responseFromServer.error();
        } else if (reservation1.getAccountId().intValue() != account.getId().intValue()) {
            return responseFromServer.illegal();
        } else {
            return reservationService.updateBuyerReservation(reservation);
        }
    }
}
