---
title: 系统设计
date: 2020/05/14 22:00 
---



# 后端设计

### 1 框架

后端实现采用SpringBoot + Mybatis Plus + MySQL，主要有controller，service，pojo 和 dao层。

![swagger](./assets/other/frame.png)

### 2 实体类

我们根据数据库的表结构设计了对应的实体类，实体类中还包含了一些表中未存在的属性，我们将所有的实体类都放在pojo包下。每个实体类的功能已在数据库设计文档部分详细介绍了，这里不再赘述。下面给出各实体类之间的联系：

<img src="./assets/pojo/pojo_package.png" alt="pojo包" style="zoom: 33%;" />

#### 2.1 A2a

![a2a](./assets/pojo/a2a.png)

#### 2.2 Account

![account](./assets/pojo/account.png)

<img src="./assets/pojo/account_reference.png" alt="account_reference" style="zoom: 50%;" />

#### 2.3 AccountNotify

![account_notify](./assets/pojo/account_notify.png)

#### 2.4 Comment

![comment](./assets/pojo/comment.png)

![comment_reference](./assets/pojo/comment_reference.png)

#### 2.5 Commodity

![commodity](./assets/pojo/commodity.png)

![commodity_reference](./assets/pojo/commodity_reference.png)

#### 2.6 Notice

![notice](./assets/pojo/notice.png)

![notice_reference](./assets/pojo/notice_reference.png)

#### 2.7 Notify

![notify](./assets/pojo/notify.png)

#### 2.8 Reservation

![reservation](./assets/pojo/reservation.png)

![reservation_reference](./assets/pojo/reservation_reference.png)

#### 2.9 Search 

![search](./assets/pojo/search.png)

#### 2.10 Token

![token](./assets/pojo/token.png)

#### 2.11 Type

![type](./assets/pojo/type.png)

### 3 DAO层

本项目DAO层均继承自BaseMapper，数据访问层总览如下：

![BaseMapper](./assets/dao/BaseMapper.png)

### 4 Service层

Service层，即服务层，通过调用DAO层实现，对一些操作进行封装。

#### 4.1 AccountService

![accountService](./assets/service/accountService.png)

#### 4.2 CommentService

![commentService](./assets/service/commentService.png)

#### 4.3 CommodityService

![commodityService](./assets/service/commodityService.png)

#### 4.4 NoticeService

![noticeService](./assets/service/noticeService.png)

#### 4.5 NotifyService

![notifyService](./assets/service/notifyService.png)

#### 4.6 ReservationService

![reservationService](./assets/service/reservationService.png)

#### 4.7 SearchService

![searchService](./assets/service/searchService.png)

#### 4.8 TokenService

![tokenService](./assets/service/tokenService.png)

### 5 Controller层

​	Controller层负责具体的业务模块流程的控制，本项目各controller展示如下：

#### 5.1 AccountController

AccountController主要处理针对于用户信息的请求：如登录，登出，注册，上传头像，更新信息等等，调用AccountService来实现相对应业务的处理。

![accountController](./assets/controller/accountController.png)

#### 5.2 CommentController

CommentController主要处理针对评论的请求，比如：获取商品的评论列表、发送评论以及删除评论。

![commentController](./assets/controller/commentController.png)

#### 5.3 CommodityController

CommodityController主要处理有关商品的请求，比如：商品信息修改、商品发布、删除商品、商品名称模糊查找、商品排序、商品类别查找、获取同一通告下全部商品等。

![commodityController](./assets/controller/commodityController.png)

#### 5.4 NoticeController

NoticeController主要处理有关通告的请求，比如创建通告、修改通告、删除通告、取消通告、获取最新发布的通告以及获取用户发布的通告等。

![noticeController](./assets/controller/noticeController.png)

#### 5.5 NotifyController

NotifyController主要处理有关通知的请求，比如获取未读消息、获取所有通知、用户查看通知后设为已读等。

![notifyController](./assets/controller/notifyController.png)

#### 5.6 ReservationController

ReservationController主要处理有关预约的请求，比如创建预约、取消预约、完成预约、获取预约记录、获取被预约记录等。

![reservationController](./assets/controller/reservationController.png)

#### 5.7 SearchController

SearchController主要处理有关历史记录的请求，比如获取用户搜索记录、删除某一历史记录、删除全部历史记录等。

![searchController](./assets/controller/searchController.png)

### 6 拦截验证

​	我们使用拦截器来处理所有的请求，自定义loginInterCeptor，实现HandlerInterceptorAdapter在preHandle中，为路径中包含以下地址的请求放行，其余的请求路径均需要进行登录验证。

``` java
 if (arg0.getRequestURI().contains("/login")
                || arg0.getRequestURI().contains("/register")
                || arg0.getRequestURI().contains("/static")
                || arg0.getRequestURI().contains("/test")) {
            return true;
        }
```

​	我们采用token的方式来维护用户的登录状态，用户请求时将token放在请求头中，

``` java
final String headerToken = arg0.getHeader("token");
```

​	如果没有token则视为未登录，拿到token后则到数据库中查询验证。

​	如下图所示，我们定义了token实体类，以及对应的dao。

![token](./assets/other/token.png)

### 7 RESTful API：Swagger

本项目后端接口设计符合RESTful API的思想，浏览器使用POST，DELETE，PUT和GET四种请求方式分别对指定的URL资源进行增删改查操作，通过URI实现对资源的管理及访问，具有扩展性强、结构清晰的特点。

本项目后端API总览：

![swagger](./assets/other/swagger.png)

以AccountController为例展现 API列表：

![swagger](./assets/other/swagger2.png)

