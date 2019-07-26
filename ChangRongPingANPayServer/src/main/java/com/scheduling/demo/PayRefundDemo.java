package com.scheduling.demo;

//~--- non-JDK imports --------------------------------------------------------

import com.google.gson.Gson;
import com.scheduling.utils.TLinx2Util;
import com.scheduling.utils.TLinxAESCoder;
import com.scheduling.utils.TLinxSHA1;
import com.scheduling.utils.TestParams;
import net.sf.json.JSONObject;

import java.util.*;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Class PayRefundDemo
 * Description 订单退款
 * Create 2017-04-19 16:50:23
 * @author Benny.YEE
 */
public class PayRefundDemo {
	
	/**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
     */
    public static void main(String[] args) {
    	String shoppass= TLinxSHA1.SHA1("123456");
    	System.out.println("shoppass----:"+shoppass);
        new PayRefundDemo().payRefund("20171225111118", "20003111462017120510040000", "订单退款测试",
  1, null, "4200000032201711144648000277",null,null,null, TLinxSHA1.SHA1("123456"));
    }								
    /**
     * Method payRefund
     * Description 说明：
     *
     * @param outNo 说明：原始订单的开发者交易流水号
     * @param refundOutNo 说明：新退款订单的开发者流水号，同一门店内唯一
     * @param refundOrdName 说明：退款订单名称，可以为空
     * @param refundAmount 说明：退款金额（以分为单位，没有小数点）
     * @param tradeAccount 说明：交易帐号（收单机构交易的银行卡号，手机号等，可为空）
     * @param tradeNo 说明：交易号（收单机构交易号，可为空）
     * @param tradeResult 说明：收单机构原始交易信息，请转换为json数据
     * @param tmlToken 说明：终端令牌，终端上线后获得的令牌
     * @param remark 说明：退款备注
     * @param shopPass 说明：主管密码，对密码进行sha1加密，默认为123456
     */
    public void payRefund(String outNo, String refundOutNo, String refundOrdName, Integer refundAmount,
                          String tradeAccount, String tradeNo, String tradeResult, String tmlToken, String remark,
                          String shopPass) {

        // 初始化参数
        String timestamp = new Date().getTime() / 1000 + "";    // 时间

        try {

            // 固定参数
            TreeMap<String, String> postmap = new TreeMap<String, String>();    // 请求参数的map

            postmap.put("open_id", TestParams.OPEN_ID);
            postmap.put("timestamp", timestamp);

            TreeMap<String, Object> datamap = new TreeMap<String, Object>();    // data参数的map

            ArrayList listsum =new ArrayList();
            Map<String, Object> dataMap0 = new HashMap<String, Object>();
            ArrayList list =new ArrayList();
           /* for(int i=1;i<4;i++){*/

                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("suborderId", "20003111462017120510040000");// 子订单号
                dataMap.put("subrefundId", "20003111462017120510040001");// 退款子订单号
                dataMap.put("subrefundamt", "0.01");// 退款子订单金额
                //dataMap.put("submasterId", "2000311146");// 子商户号
                dataMap.put("object", "飞机票");// 订单详情

                //dataMap.put("SubAccNo", "入账会员子账户");// 子帐号
                dataMap.put("refundModel", "0-从冻结退款 1-普通退款");// 支付模式
                dataMap.put("RefundTranFee", "0.01");// 从手续费退款金额
                list.add(dataMap);


            /*}*/
            dataMap0.put("oderlist", list);
            dataMap0.put("remarktype", "备注类型-JHT0100000");
            //dataMap0.put("plantCode", "平台代码");
            dataMap0.put("SFJOrdertype",  "1");
            listsum.add(dataMap0);

            Gson gson = new Gson();
            String json = gson.toJson(listsum);


            datamap.put("cmd", listsum);
            datamap.put("out_no", outNo);
            datamap.put("refund_out_no", refundOutNo);
            datamap.put("refund_ord_name", refundOrdName);
            datamap.put("refund_amount", refundAmount + "");
            datamap.put("trade_account", tradeAccount);
            datamap.put("trade_no", tradeNo);
            datamap.put("trade_result", tradeResult);
            datamap.put("tml_token", tmlToken);
            datamap.put("remark", remark);
            datamap.put("shop_pass", shopPass);
            
            /**
             * 1 data字段内容进行AES加密，再二进制转十六进制(bin2hex)
             */
            TLinx2Util.handleEncrypt(datamap, postmap);
            
            postmap.put("sign_type", "RSA");

            /**
             * 2 请求参数签名 按A~z排序，串联成字符串，进行RSA签名
             */
            TLinx2Util.handleSignRSA(postmap);

            /**
             * 3 请求、响应
             */
            String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYREFUND);

            System.out.println("返回字符串=" + rspStr);

            /**
             * 4 验签  有data节点时才验签
             */
            JSONObject respObject = JSONObject.fromObject(rspStr);
            Object dataStr    = respObject.get("data");

            if (!rspStr.isEmpty() && ( dataStr != null )) {
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
