package com.scheduling.common.util;


import org.springframework.http.HttpRequest;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

public class UploadFile {


    public String uploadFile(HttpServletRequest request,@RequestParam(value = "idcard_front") MultipartFile file) throws IllegalArgumentException,
            Exception {
        // 参数列表
//        File path2 = new File(ResourceUtils.getURL("classpath:").getPath());
//        String absolutePath = path2.getAbsolutePath();
        //用户的当前工作目录
        String property = System.getProperty("user.dir");
        // 获取当前操作系统名称
        String os = System.getProperty("os.name");
        String path="";
        String filename= UUID.randomUUID()+".jpg";
        //判断是什么操作系统
        if(os.toLowerCase().startsWith("win")){
            path = property+"\\src\\main\\resources\\static\\"+filename;
        }else{
            path ="/usr/local/webserver/nginx/html/images/"+filename;
        }

        File tempFile = new File(path);
        file.transferTo(tempFile);
        return filename;
    }



}

