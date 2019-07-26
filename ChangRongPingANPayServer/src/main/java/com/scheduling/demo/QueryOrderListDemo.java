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
 * Class QueryOrderListDemo
 * Description 订单列表
 * Create 2017-04-19 14:40:25
 * @author Benny.YEE
 */
public class QueryOrderListDemo {
	
	 /**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
     */
    public static void main(String[] args) {
        new QueryOrderListDemo().queryOrderList(null,null,null,null,null,null,null,null);
    }

    /**
     * Method queryOrderList
     * Description 说明：
     *
     * @param outNo 说明：开发者流水号 可为空
     * @param tradeNo 说明：交易单号（收单机构交易号，可为空）
     * @param ordNo 说明：付款订单号
     * @param pmtTag 说明：支付方式标签
     * @param ordType 说明：交易方式1消费，2辙单（退款）
     * @param status 说明：订单状态（1交易成功，2待支付，4已取消，9等待用户输入密码确认）
     * @param sdate 说明：订单开始日期
     * @param edate 说明：订单结束日期
     */
    public void queryOrderList(String outNo, String tradeNo, String ordNo, String pmtTag, String ordType,
                               String status, String sdate, String edate) {

        // 初始化参数
        String timestamp = new Date().getTime() / 1000 + "";    // 时间

        try {
            // 固定参数
            TreeMap<String, String> postmap = new TreeMap<String, String>();//请求参数的map
            postmap.put("open_id", TestParams.OPEN_ID);
            postmap.put("timestamp", timestamp);

            TreeMap<String, Object> datamap = new TreeMap<String, Object>();//data参数的map
            datamap.put("out_no", outNo);
            datamap.put("trade_no", tradeNo);
            datamap.put("ord_no", ordNo);
            datamap.put("pmt_tag", pmtTag);
            datamap.put("ord_type", ordType);
            datamap.put("status", status);
            datamap.put("sdate", sdate);
            datamap.put("edate", edate);

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
            String rspStr = TLinx2Util.handlePost(postmap, TestParams.ORDERLIST);

            System.out.println("返回字符串=" + rspStr);

            /**
             * 4 验签  有data节点时才验签
             */
            JSONObject respObject = JSONObject.fromObject(rspStr);
            System.out.println("===响应错误码：" + respObject.get("errcode"));
            System.out.println("===响应错误提示：" + respObject.get("msg"));
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
