package com.scheduling.demo;

import com.scheduling.utils.TLinx2Util;
import com.scheduling.utils.TLinxAESCoder;
import com.scheduling.utils.TestParams;
import net.sf.json.JSONObject;

import java.util.Date;
import java.util.TreeMap;
/**
 * 门店支付方式列表 paylist
 * @author EX_KJKFB_LIUJIAJIA
 *
 */
public class QueryPayListDemo {
	

    public static void main(String[] args){
        new QueryPayListDemo().queryPayList();
    }

    public void queryPayList(){
        // 初始化参数
        String pmtType   = "0,1,2,3,4,5";
        String timestamp = new Date().getTime() / 1000 + "";    // 时间

        try {
            // 固定参数
            TreeMap<String, String> postmap = new TreeMap<String, String>();//请求参数的map
            postmap.put("open_id", TestParams.OPEN_ID);
            postmap.put("timestamp", timestamp);

            TreeMap<String, Object> datamap = new TreeMap<String, Object>();//data参数的map
            datamap.put("pmt_type", pmtType);

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
             */
            String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYLIST);
            System.out.println("返回字符串="+rspStr);

            /**
             * 4 验签  有data节点时才验签
             */
            JSONObject respObject = JSONObject.fromObject(rspStr);
            Object dataStr    = respObject.get("data");

            if (!rspStr.isEmpty() || (dataStr != null)) {
                if (TLinx2Util.verifySign(respObject)) {    // 验签成功

                    /**
                     * 5 AES解密，并hex2bin
                     */
                    String respData = TLinxAESCoder.decrypt(dataStr.toString(), TestParams.OPEN_KEY);
                    
                    System.out.println("==================响应data内容:" +respData );
                    
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
