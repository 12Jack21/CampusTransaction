package com.example.transaction.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.transaction.dto.SimpleCondition;
import com.example.transaction.dto.reservation.DetailedReservation;
import com.example.transaction.pojo.*;
import com.example.transaction.service.CommodityService;
import com.example.transaction.service.NoticeService;
import com.example.transaction.service.ReservationService;
import com.example.transaction.util.code.ReservationCode;
import com.example.transaction.util.jsonParamResolver.handler.RequestJson;
import com.example.transaction.util.responseFromServer;
import com.example.transaction.service.impl.AccountVerify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
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
    @ApiImplicitParam(name = "reservation", value = "@RequestBody 预约请求体", paramType = "Reservation", dataType = "Reservation")
    @PostMapping
    public responseFromServer setupReservation(@RequestBody Reservation reservation, HttpServletRequest request) {
//        Account account = (Account) request./* 修改获取账号方式*/getAttribute("currentAccount");
        Account account = accountVerify.getCurrentAccount(request);
        if (reservation.getCommodityId() == null || reservation.getCount() == null) {
            return responseFromServer.error();
        } else {
            /**
             * ZZH
             * TODO : 先直接用传来的数据里的用户id
             */
//            reservation.setAccountId(account.getId());
            
            reservation.setStateEnum(ReservationCode.WAITING.getCode());
            return reservationService.setUpReservation(reservation);
        }
    }

    /**
     * 取消预约
     *
     * @param reservationId
     * @param request
     * @return
     */
//    @RequestMapping("/cancelReservation")
    @ApiOperation(value = "取消预约")
    @PutMapping("/{reservationId}/cancel")
    public responseFromServer cancelReservation(@PathVariable Integer reservationId, HttpServletRequest request) {
//        Account account = (Account) request./* 修改获取账号方式*/getAttribute("currentAccount");
        Account account = accountVerify.getCurrentAccount(request);
        if (reservationId != null) {
            return reservationService.cancelReservation(reservationId, account.getId());
        } else {
            return responseFromServer.error();
        }
    }


    /**
     * 取消预约
     *
     * @param reservationId
     * @param request
     * @return
     */
//    @RequestMapping("/cancelReservation")
    @ApiOperation(value = "取消预约")
    @PutMapping("/{reservationId}/fail")
    public responseFromServer failReservation(@PathVariable Integer reservationId, HttpServletRequest request) {
//        Account account = (Account) request./* 修改获取账号方式*/getAttribute("currentAccount");
        Account account = accountVerify.getCurrentAccount(request);
        if (reservationId != null) {
            return reservationService.cancelReservation(reservationId, account.getId());
        } else {
            return responseFromServer.error();
        }
    }


    /**
     * 删除预约
     *
     * @param reservationId
     * @param request
     * @return
     */
    @ApiOperation(value = "删除预约")
    @DeleteMapping("/{reservationId}")
    public responseFromServer deleteReservation(@PathVariable Integer reservationId, HttpServletRequest request) {
        Reservation reservation = verifySeller(reservationId, request);
        if (reservation != null) {
            /*在验证的时候已经更新reservation信息*/
            return reservationService.deleteReservation(reservation);
        } else {
            return responseFromServer.illegal();
        }
    }


    /**
     * 通过reservationId 验证请求用户是否是对应的卖家
     *
     * @param reservationId
     * @param request
     * @return
     */
    @ApiOperation(value = "用户身份验证")
    @GetMapping("/{reservationId}/verify")
    public Reservation verifySeller(@PathVariable Integer reservationId, HttpServletRequest request) {
        Reservation reservation = new Reservation(reservationId);
//        Account account = (Account) request./* 修改获取账号方式*/getAttribute("currentAccount");
        Account account = accountVerify.getCurrentAccount(request);
        if (reservation.getId() == null) {
            return null;
        } else {
            responseFromServer response = reservationService.getDetailedReservation(reservation.getId());
            if (response.isSuccess()) {
                /*用户身份验证*/
                reservation = (Reservation) response.getData();
                Commodity commodity = reservation.getCommodity();
                if (commodity != null
                        && commodity.getNotice() != null
                        && commodity.getNotice().getAccountId() != null) {
                    if (commodity.getNotice().getAccountId().intValue() != account.getId().intValue())
                        /*此时要操作的用户跟notice的卖家不符合 非法操作*/ {
                        return null;
                    }
                } else {
                    /*查询错误*/
                    return null;
                }
                /*用户验证成功*/
                return reservation;
            } else {
                return null;
            }
        }

    }

    /**
     * 设置预约成功----减少库存
     *
     * @param reservationId
     * @param request
     * @return
     */

    @ApiOperation(value = "预约成功，减小库存")
    @ApiImplicitParam(name = "reservationId", value = "预约Id", paramType = "Integer", dataType = "Integer")
    @PutMapping("/{reservationId}/confirm")
    public responseFromServer validateReservation(@PathVariable Integer reservationId, HttpServletRequest request) {
        Reservation reservation = verifySeller(reservationId, request);
        if (reservation != null) {
            /*验证当前操作用户是否是卖家*/
//            return reservationService.validateReservation(reservation, ((Account) request./*  修改获取账号方式*/getAttribute("currentAccount")).getId());
            return reservationService.validateReservation(reservation, accountVerify.getCurrentAccount(request).getId());
        } else {
            return responseFromServer.illegal();
        }
    }


    @ApiOperation(value = "更改评价")
    @ApiImplicitParam(name = "reservationId", value = "预约Id", paramType = "Integer", dataType = "Integer")
    @PutMapping("/{reservationId}")
    public responseFromServer updateEvaluation(@PathVariable Integer reservationId,
                                               Double evaluationSell,
                                               Double evaluationBuy,
                                               HttpServletRequest request) {
        Reservation reservation = new Reservation(reservationId);
        if (evaluationBuy != null && evaluationBuy >= 0 && evaluationBuy <= 5) {
            reservation.setEvaluationBuy(evaluationBuy);
        }
        if (evaluationSell != null && evaluationSell >= 0 && evaluationSell <= 5) {
            reservation.setEvaluationSell(evaluationSell);
        }
        /*暂时没有验证身份*/
        return reservationService.updateReservation(reservation);

    }


    /**
     * 卖家设置预约为完成
     *
     * @param reservationId
     * @param request
     * @return
     */
//    @RequestMapping("/finishReservation")
    @ApiOperation("预约完成")
    @ApiImplicitParam(name = "reservationId", value = "预约Id", paramType = "Integer", dataType = "Integer")
    @PutMapping("/{reservationId}/complete")
    public responseFromServer finishReservation(@PathVariable Integer reservationId, HttpServletRequest request) {
        Reservation reservation = verifySeller(reservationId, request);
        /*验证当前操作用户是否是卖家*/
        if (reservation != null) {
            return reservationService.finishReservation(reservation.getId());
        } else {
            return responseFromServer.illegal();
        }
    }

    /**
     * 查看当前商品的所有预约
     *
     * @param commodityId
     * @param pageIndex
     * @param request
     * @return
     */
//    @RequestMapping("/getReservationPageForCommodity")
    @ApiOperation(value = "查看当前商品的所有预约")
    @GetMapping("/commodity/{commodityId}")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "commodityId", value = "商品Id", paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
            }
    )
    public responseFromServer getReservationPageForCommodity(@PathVariable Integer commodityId,
                                                             @RequestJson Integer pageIndex,
                                                             HttpServletRequest request) {
//        Commodity commodity = (Commodity) map.get("commodity");
//        Integer pageIndex = (Integer) map.get("pageIndex");
        if (commodityId == null) {
            return responseFromServer.error();
        }
        pageIndex = pageIndex == null || pageIndex.intValue() <= 0 ? 1 : pageIndex;
        /*用户核对*/
        responseFromServer response = commodityService.getSimpleCommodity(commodityId);
        if (!response.isSuccess()) {
            return responseFromServer.error();
        }
        Commodity commodity = (Commodity) response.getData();
        Notice notice = (Notice) noticeService.getSimpleNotice(commodity.getNoticeId()).getData();
        if (notice.getAccountId().intValue() == accountVerify.getCurrentAccount(request).getId().intValue()) {
            /*验证成功*/
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("commodity_id", commodity.getId());
            return reservationService.getSimpleReservationPage(queryWrapper, pageIndex);
        } else {
            return responseFromServer.illegal();
        }
    }

    /**
     * 查看我申请的预约
     *
     * @param accountId
     * @param condition
     * @param request
     * @return
     */
//    @RequestMapping("/getMyReservation")
    @ApiOperation(value = "申请预约列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "accountId", value = "用户Id", paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "pageIndex", value = "页面索引", paramType = "Integer", dataType = "Integer")
            }
    )
    @GetMapping("/account/{accountId}")
    public responseFromServer getMyReservation(@PathVariable Integer accountId,
                                               SimpleCondition condition,
                                               HttpServletRequest request) {
        /**
         * ZZH
         * TODO : token后直接根据token获取用户
         * Account account =  accountVerify.getCurrentAccount(request);
         */
        Account account = accountVerify.verifyWithReturn(new Account(accountId), request);
        Integer pageIndex = condition.getPageIndex();
        pageIndex = pageIndex == null || pageIndex.intValue() <= 0 ? 1 : pageIndex;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id", account.getId());
        queryWrapper.eq("state_enum", condition.getType());
//        Boolean isCommodity = (Boolean) map.get("isCommodity");
        return reservationService.getReservationsPage(queryWrapper, pageIndex);
    }


    /**
     * 查看我接收到的预约
     *
     * @param condition
     * @param accountId
     * @param request
     * @return
     */
    @ApiOperation(value = "接收预约列表")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "accountId", value = "用户Id", paramType = "Integer", dataType = "Integer"),
                    @ApiImplicitParam(name = "page_index", value = "页面索引", paramType = "Integer", dataType = "Integer")
            }
    )
//    @GetMapping("/account/{accountId}/receive")
    @GetMapping("/account/{accountId}/receive")
    public responseFromServer getReservationRequest(SimpleCondition condition,
                                                    @PathVariable Integer accountId,
                                                    HttpServletRequest request) {
        Integer pageIndex = condition.getPageIndex();
        condition.setPageIndex(pageIndex == null || pageIndex.intValue() <= 0 ? 1 : pageIndex);
        /**
         * TODO : token过后直接根据token获取当前的id
         * return reservationService.getReservationRequest(accountVerify.getCurrentAccount(request).getId(), pageIndex);
         */
        return reservationService.getReservationRequest(accountId, condition);
    }

    /**
     * 获取详细的预约内容
     *
     * @param reservationId
     * @param request
     * @return
     */
    @ApiOperation("获取详细预约内容")
    @ApiImplicitParam(name = "reservationId", value = "预约Id", paramType = "Integer", dataType = "Integer")
    @GetMapping("/{reservationId}")
    public responseFromServer getDetailedReservation(@PathVariable Integer reservationId,
                                                     HttpServletRequest request) {
        responseFromServer response = reservationService.getDetailedReservationInfo(reservationId);
        if (response.isSuccess()) {
            DetailedReservation detailedReservation = (DetailedReservation) response.getData();
            Account currentAccount = accountVerify.getCurrentAccount(request);
            if (!currentAccount.getId().equals(detailedReservation.getAccountId())
                    && !currentAccount.getId().equals(detailedReservation.getBuyerId())) {
                /*当前用户不是卖家或买家*/
                return responseFromServer.error();
            }
            return responseFromServer.success(detailedReservation);
        }
        return responseFromServer.error();
    }

    /**
     * 获取简单预约内容
     *
     * @param reservationId
     * @param request
     * @return
     */
//    @RequestMapping("/getSimpleReservation")
    @ApiOperation(value = "获取简单预约内容")
    @ApiImplicitParam(name = "reservationId", value = "预约Id", paramType = "Integer", dataType = "Integer")
    @GetMapping("/simple/{reservationId}")
    public responseFromServer getSimpleReservation(@PathVariable Integer reservationId, HttpServletRequest request) {
        /**
         * ZZH
         * TODO : 为什么获取简单预约需要验证卖家身份?
         */
        Reservation reservation = verifySeller(reservationId, request);
        if (reservation != null) {
            reservation = new Reservation(reservationId);
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
    @ApiImplicitParam(name = "reservation", value = "预约内容", paramType = "Integer", dataType = "Integer")
    @PutMapping("/update")
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
