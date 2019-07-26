package com.scheduling.config;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class Base64 {
    /***
     * encode by Base64
     */
    public static String encode(byte[]input){
        try{
            Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
            Method mainMethod= clazz.getMethod("encode", byte[].class);
            mainMethod.setAccessible(true);
            Object retObj=mainMethod.invoke(null, new Object[]{input});
            return (String)retObj;
        }catch (Exception e){

        }
        return "";
    }
    /***
     * decode by Base64
     */
    public static byte[] decode(String input) throws Exception{
        try{
            Class clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
            Method mainMethod= clazz.getMethod("decode", String.class);
            mainMethod.setAccessible(true);
            Object retObj=mainMethod.invoke(null, input);
            return (byte[])retObj;
        }catch (Exception e){

        }
        return "".getBytes();
    }
    public static String getCurrentLoginUserid(HttpServletRequest request){
        Object userId = request.getAttribute("userid");
        if(userId == null){
            userId = "-1";
        }
        return userId.toString();
    }
}
