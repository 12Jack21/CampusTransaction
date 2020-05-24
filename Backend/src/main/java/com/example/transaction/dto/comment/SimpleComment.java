package com.example.transaction.dto.comment;

import com.example.transaction.dto.account.SimpleAccount;
import com.example.transaction.pojo.Comment;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: SimpleComment
 * @Description: 简单评论对象
 * @Author: 曾志昊
 * @Date: 2020/5/24 15:11
 */
@Data
public class SimpleComment implements Serializable {
    Integer fromId = -1;
    String fromName = "";
    String fromImage = "";
    String toName = "";
    String content = "";
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    Date date;

    public SimpleComment(){}

    public SimpleComment(Comment comment){
        this.fromId = comment.getFromId();
        if(comment.getSender()!=null){
            SimpleAccount sender = comment.getSender();
            this.fromImage = sender.getAvatar();
            this.fromName = sender.getUsername();
        }
        if(comment.getReceiver()!=null){
            SimpleAccount receiver  = comment.getReceiver();
            this.toName = receiver.getUsername();
        }
        this.content = comment.getContent();
        this.date = comment.getTime();
    }

}
