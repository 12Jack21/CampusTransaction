package com.example.transaction.util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;

/**
 * @ClassName: responseFromServer
 * @Description: 封装高可复用的服务端响应类
 * @Author: 曾志昊
 * @Date: 2020/4/25 17:55
 */
@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
public class responseFromServer<T> implements Serializable {

    private int status;//0 成功 1 失败 10 需要登录
    private String msg;
    private T data;

    private responseFromServer(int status){
        this.status = status;
    }
    private responseFromServer(int status, T data){
        this.status = status;
        this.data = data;
    }

    private responseFromServer(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private responseFromServer(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    public static <T> responseFromServer<T> needLogin(){
        return new responseFromServer<T>(ResponseCode.NEED_LOGIN.getCode());
    }

    public static <T> responseFromServer<T> success(){
        return new responseFromServer<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> responseFromServer<T> success(String msg){
        return new responseFromServer<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> responseFromServer<T> success(T data){
        return new responseFromServer<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> responseFromServer<T> success(String msg, T data){
        return new responseFromServer<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }


    public static <T> responseFromServer<T> error(){
        return new responseFromServer<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    public static <T> responseFromServer<T> error(String errorMessage){
        return new responseFromServer<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> responseFromServer<T> error(int errorCode, String errorMessage){
        return new responseFromServer<T>(errorCode,errorMessage);
    }

}
