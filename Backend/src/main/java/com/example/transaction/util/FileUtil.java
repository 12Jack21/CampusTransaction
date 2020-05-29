package com.example.transaction.util;

import com.example.transaction.util.code.ResourcePath;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @ClassName: FileUtil
 * @Description: 上传和下载图片文件工具
 * @Author: 曾志昊
 * @Date: 2020/5/18 16:41
 */
public class FileUtil {
    public static responseFromServer checkImageFile(MultipartFile file, boolean update,boolean isAvatar) {
        /*如果是上传到已经创建的商品,直接存到images文件夹下*/
        String filePath = update ? (isAvatar? ResourcePath.avatarFilePath:ResourcePath.goodsFilePath ): ResourcePath.imageTempPath;
        //获取文件后缀
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
            return responseFromServer.error(0, "请选择jpg,jpeg,gif,png格式的图片");
        }
        File savePathFile = new File(filePath);
        if (!savePathFile.exists()) {
            //若不存在该目录，则创建目录
            savePathFile.mkdir();
        }
        //通过UUID生成唯一文件名
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + suffix;
        return responseFromServer.success(filename);
    }

    public static responseFromServer saveFile(MultipartFile file, boolean update, String fileName, boolean isAvatar) {
        String filePath = update ? (isAvatar? ResourcePath.avatarFilePath:ResourcePath.goodsFilePath ): ResourcePath.imageTempPath;
        try {
            //将文件保存指定目录
            file.transferTo(new File(filePath + fileName));
        } catch (Exception e) {
            e.printStackTrace();
            return responseFromServer.error(0, "保存文件异常");
        }
        return responseFromServer.success(fileName);
    }
}
