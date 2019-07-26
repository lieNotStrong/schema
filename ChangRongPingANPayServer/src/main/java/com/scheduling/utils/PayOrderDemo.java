package com.scheduling.utils;

import com.google.gson.Gson;
import net.sf.json.JSONObject;

import java.net.URLEncoder;
import java.util.*;

/**
 * Class PayOrderDemo
 * Description
 * Create 2017-04-19 15:58:31
 * @author Benny.YEE
 */
public class PayOrderDemo {
	 /**
     * Method main  
     * Description 说明：
     *
     * @param args 说明： jump_url和auth_code都为空，就是商户生成二维码(被扫),auth_code有值就是扫客户的付款码(主扫)，jump_url有值就是jspay(
     */
	 //20171225111118
    public static void main(String[] args) {
        new PayOrderDemo().order("20171225111138", "WeixinOL", null, "微信支付测试(服务器测试)",3,
                0, 0, 3,"201712250001", "4200000032201711144648000277", null, "下单接口",
                null, null, null, "http://localhost:8085/demo111/callback.do","123142345324,23453576586565","0.01,0.02");
//        new PayOrderDemo().queryOrder("20171212111111", "AlipayCS", "自定义付款方式名称", "支付宝支付测试(pay_time)",1,
//                0, 0, 1,"20171208000000", "4200000032201711144648000277", null, "支付宝下单接口",
//                "", "订单标记", null, "http://10.14.158.27:8085/tlinx2apidemo1/callback/scanpay_cashier/payResult");
    }

    /**
     * Method queryOrderList
     * Description 说明：
     *
     * @param outNo 说明：开发者流水号，确认同一门店内唯一
     * @param pmtTag 说明：付款方式编号
     *
     *
     * @param pmtName 说明：商户自定义付款方式名称
     * @param ordName 说明：订单名称（描述）
     * @param originalAmount 说明：原始交易金额（以分为单位，没有小数点）
     * @param discountAmount 说明：折扣金额（以分为单位，没有小数点）
     * @param ignoreAmount 说明：抹零金额（以分为单位，没有小数点）
     * @param tradeAmount 说明：实际交易金额（以分为单位，没有小数点）
     * @param tradeAccount 说明：交易帐号（收单机构交易的银行卡号，手机号等，可为空）
     * @param tradeNo 说明：交易号（收单机构交易号，可为空）
     * @param tradeResult 说明：交易返回结果
     * @param remark 说明：订单备注
     * @param authCode 说明：条码支付的授权码（条码抢扫手机扫到的一串数字）
     * @param tag 说明：订单标记，订单附加数据
     * @param jumpUrl 说明：公众号/服务窗支付必填参数，支付结果跳转地址
     * @param notifyUrl 说明：异步通知地址
     */
    public Object order(String outNo, String pmtTag, String pmtName, String ordName, Integer originalAmount,
                               Integer discountAmount, Integer ignoreAmount, Integer tradeAmount, String tradeAccount,
                               String tradeNo, String tradeResult, String remark, String authCode, String tag,
                               String jumpUrl, String notifyUrl,String suborderId,String subamount) {

        String[] suborderIds = suborderId.split(",");
        String[] subamounts = subamount.split(",");

        // 初始化参数
        String timestamp = new Date().getTime() / 1000 + "";    // 时间

        try {

            // 固定参数
            TreeMap<String, String> postmap = new TreeMap<String, String>();    // 请求参数的map

            postmap.put("open_id", TestParams.OPEN_ID);
            postmap.put("timestamp", timestamp);

            TreeMap<String, Object> datamap = new TreeMap<String, Object>();    // data参数的map
/*String suborderId;
		String subrefundId;
		String subrefundamt;
		//String submasterId;
		String object;
		String PayModel;
		String TranFee;
		String remarktype;
		String SFJOrdertype;*/

            ArrayList listsum =new ArrayList();
            Map<String, Object> dataMap0 = new HashMap<String, Object>();
            ArrayList list =new ArrayList();

            for(int i=0;i<suborderIds.length;i++){
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("suborderId", suborderIds[i]);// 子订单号
                //dataMap.put("subrefundId", "20003111462017120510040001");// 退款子订单号
                dataMap.put("subamount", subamounts[i]);// 子订单金额
                //dataMap.put("submasterId", "2000311146");// 子商户号
                dataMap.put("object", "飞机票");// 订单详情
                dataMap.put("SubAccNo", "3144000000001089");// 子帐号
                dataMap.put("PayModel", "1");// 支付模式
                dataMap.put("TranFee", "0.01");// 手续费
                list.add(dataMap);
            }
            dataMap0.put("oderlist", list);
            dataMap0.put("remarktype", "备注类型-SDS0100000");
            dataMap0.put("plantCode", "3144");
            dataMap0.put("SFJOrdertype",  "1");
            listsum.add(dataMap0);
            Gson gson = new Gson();
            String json = gson.toJson(listsum);
            System.out.println(json);
            datamap.put("cmd", listsum);
            datamap.put("out_no", outNo);
            datamap.put("pmt_tag", pmtTag);
            datamap.put("pmt_name", pmtName);
            datamap.put("ord_name", ordName);
            datamap.put("original_amount", originalAmount+"");
            datamap.put("discount_amount", discountAmount+"");
           datamap.put("ignore_amount", ignoreAmount+"");
            datamap.put("trade_amount", tradeAmount+"");
            datamap.put("trade_account", tradeAccount+"");
            datamap.put("trade_no", tradeNo);
            datamap.put("trade_result", tradeResult); // 新增字段
            remark=URLEncoder.encode(remark, "utf-8");
           datamap.put("remark", remark);
            
            //  jump_url和auth_code都为空，就是商户生成二维码(被扫)
            
            datamap.put("auth_code", authCode);      
            datamap.put("tag", tag);
            datamap.put("jump_url", jumpUrl);
            datamap.put("notify_url", notifyUrl);
            datamap.put("sub_appid", "283074331");
            datamap.put("trade_type", "APP");

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
            String rspStr = TLinx2Util.handlePost(postmap, TestParams.PAYORDER);

            System.out.println("返回字符串=" + rspStr);

            /**
             * 4 验签  有data节点时才验签
             */
            JSONObject respObject = JSONObject.fromObject(rspStr);
            
            Object dataStr    = respObject.get("data");
          //  System.out.println("dataStr"+dataStr);

            if (!rspStr.isEmpty() && ( dataStr != null )) {
                if (TLinx2Util.verifySign(respObject)) {    // 验签成功

                    /**
                     * 5 AES解密，并hex2bin
                     */
                    String respData = TLinxAESCoder.decrypt(dataStr.toString(), TestParams.OPEN_KEY);
                    
                    System.out.println("==================响应data内容:" +respData.toString());
                    return respData.toString();
                } else {
                    System.out.println("=====验签失败=====");
                    return "验签失败";
                }
            } else {
                System.out.println("=====没有返回data数据=====");
                return "没有返回data数据";
            }
        } catch (Exception e) {
           e.printStackTrace();
            return e.getMessage().toString();
        }
    }
    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }
   
}


//~ Formatted by Jindent --- http://www.jindent.com
