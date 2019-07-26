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
 * Class QueryPayStatusDemo
 * Description 查询订单状态
 * Create 2017-04-19 16:29:16
 * @author Benny.YEE
 */
public class QueryPayStatusDemo {
	
	 /**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
     */
    public static void main(String[] args) {
        new QueryPayStatusDemo().queryPayStatus("", "17121300101724");    // 二者填一个参数就行
    }

    /**
     * Method queryPayStatus
     * Description 说明：
     *
     * @param ordNo 说明：订单号            二者必须填一个
     * @param outNo 说明：开发者流水号       二者必须填一个（交易或退款订单）
     */
    public void queryPayStatus(String ordNo, String outNo) {

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

            /**
             * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
             */
            TLinx2Util.handleSign(postmap);

            /**
             * 3 请求、响应
             *
             */
            String rspStr = TLinx2Util.handlePost(postmap, TestParams.QUERYPAYSTATUS);

            System.out.println("返回字符串=" + rspStr);

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

   
}


//~ Formatted by Jindent --- http://www.jindent.com
