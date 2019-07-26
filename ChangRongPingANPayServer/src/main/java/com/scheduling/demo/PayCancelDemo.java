package com.scheduling.demo;

//~--- non-JDK imports --------------------------------------------------------

import com.scheduling.utils.TLinx2Util;
import com.scheduling.utils.TLinxAESCoder;
import com.scheduling.utils.TestParams;
import net.sf.json.JSONObject;

import java.util.Date;
import java.util.TreeMap;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Class PayCancelDemo
 * Description
 * Create 2017-04-19 16:45:25
 * @author Benny.YEE
 */
public class PayCancelDemo {

    /**
     * Method payCancel
     * Description 说明：
     *
     * @param ordNo 说明：订单号            二者必须填一个
     * @param outNo 说明：开发者流水号       二者必须填一个
     */
    public void payCancel(String ordNo, String outNo) {

        // 初始化参数
        String timestamp = new Date().getTime() / 1000 + "";    // 时间

        try {

            // 固定参数
            TreeMap<String, String> postmap = new TreeMap<String, String>();    // 请求参数的map

            postmap.put("open_id", TestParams.OPEN_ID);
            postmap.put("timestamp", timestamp);

            TreeMap<String, Object> datamap = new TreeMap<String, Object>();    // data参数的map

            datamap.put("out_no", outNo);
            datamap.put("ord_no", ordNo);
            
            /**
             * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
             */
            TLinx2Util.handleEncrypt(datamap, postmap);

            //签名方式
            postmap.put("sign_type", "RSA");
            System.out.println("postmap---:"+postmap);
            
            /**
             * 2 请求参数签名 按A~z排序，串联成字符串，先进行RSA签名
             */
            TLinx2Util.handleSignRSA(postmap);

            /**
             * 3 请求、响应
             */
            String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYCANCEL);

            System.out.println("返回字符串=" + new String(rspStr.getBytes("UTF-8")));
            
            
            /**
             * 4 验签  有data节点时才验签
             */
            JSONObject respObject = JSONObject.fromObject(rspStr);
            Object dataStr    = respObject.get("data");

            if (!rspStr.isEmpty() || ( dataStr != null )) {
                if (TLinx2Util.verifySign(respObject)) {    // 验签成功

                    /**
                     * 5 AES解密，并hex2bin
                     */
                    String respData = TLinxAESCoder.decrypt(dataStr.toString(), TestParams.OPEN_KEY);

                    System.out.println("==================响应data内容:" + respData);
                } else {
                    System.out.println("=====验签失败=====");
                }
            } else {
                System.out.println("=====没有返回data数据=====");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
     */
    public static void main(String[] args) {
        new PayCancelDemo().payCancel("9151306532393311893612953", "");    // 二者填一个参数就行
    }									
}


//~ Formatted by Jindent --- http://www.jindent.com
