package com.scheduling.demo;

import com.scheduling.utils.TLinx2Util;
import com.scheduling.utils.TLinxAESCoder;
import com.scheduling.utils.TestParams;
import net.sf.json.JSONObject;

import java.util.Date;
import java.util.TreeMap;

/**
 * 获取用户的授权码
 * @author EX_KJKFB_LIUJIAJIA
 *
 */
public class AuthtoopenidDemo {
	
	 /**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
	 * pmt_tag：付款方式标签
     * auth_code：条码支付的授权码
     * sub_appid：原声jsapi公众号appid
	 *
	 *
     */
    public static void main(String[] args) {
        new AuthtoopenidDemo().getOpenid("WeixinCS", "134614202897000499","");
    }

    
    public void getOpenid(String pmt_tag, String auth_code,String sud_appid) {

        // 初始化参数
        String timestamp = new Date().getTime() / 1000 + "";    // 时间

        try {

            // 固定参数
            TreeMap<String, String> postmap = new TreeMap<String, String>();    // 请求参数的map

            postmap.put("open_id", TestParams.OPEN_ID);
            postmap.put("timestamp", timestamp);

            TreeMap<String, Object> datamap = new TreeMap<String, Object>();    // data参数的map

            datamap.put("pmt_tag", pmt_tag);
            datamap.put("auth_code", auth_code);
            datamap.put("sud_appid", sud_appid);

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
            String rspStr = TLinx2Util.handlePost(postmap, TestParams.AUTHTOOPEN);

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

