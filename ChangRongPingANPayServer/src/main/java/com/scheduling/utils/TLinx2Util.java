package com.scheduling.utils;

//~--- non-JDK imports --------------------------------------------------------

import net.sf.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

//~--- JDK imports ------------------------------------------------------------

//~--- classes ----------------------------------------------------------------

/**
 * Class TLinx2Util
 * Description
 * Create 2017-03-07 14:01:23
 * @author Benny.YEE
 */
public class TLinx2Util {



    /**
     * 交易签名
     * @param postMap
     * @return
     */
    public static String sign(Map<String, String> postMap) {
        String sortStr = null;
        String sign    = null;

        try {

            /**
             * 1 A~z排序(已加上open_key)
             */
            sortStr = TLinxUtil.sort(postMap);
            System.out.println("--A~z排序后字符串--"+sortStr);

            /**
             * 2 sha1加密(小写)
             */
            String sha1 = TLinxSHA1.SHA1(sortStr);
            System.out.println("--sha1加密后--"+sha1);

            /**
             * 3 md5加密(小写)
             */
            sign = MD5.MD5Encode(sha1).toLowerCase();
            System.out.println("--MD5加密后--"+sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
    
    public static String signRSA(Map<String, String> postMap) {
        String sortStr = null;
        String sign    = null;
        try {

            /**
             * 1 A~z排序(加上open_key)
             */
            sortStr = TLinxUtil.sort(postMap);
            /**
             * 3 RSA加密(小写),二进制转十六进制
             */
            sign = TLinxRSACoder.sign(sortStr.getBytes("utf-8"), TestParams.PRIVATE_KEY);
            System.out.println("RSA签名sign:"+sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
    
    public static String signRSA1(Map<String, String> postMap) {
        String sortStr = null;
        String sign    = null;
        try {

            /**
             * 1 A~z排序(加上open_key)
             */
            sortStr = "data=5930A899FFF9F828F5BC136B8F58C04BC75ED13B144501C65DC7D0E3D94B32B5F1D04EE2B022C8A2DD8A8FBB416328E6DF4F21A0A568EBCF312B25C931685030B369ED242CD1529537E3F060365DB6DB2DFA1ADD5D594EF4B78B1A8352FDF176750501D578A380E64DB1E87E6FDE2543C2F0D4D4ACB6C914563B48E54C0DF8EAB16A818939650EB70519F2BD691905078D74B5BAE11B37B4CB2D9970C0ECC540A8E85095F25EAA618A05668199C3A845FE15EED9E3CF5F0710198C9897D18AD4EFBFF91FEEA36CE527E3B2D6D86F5302030FF80B495B565142008AF8A7597743F6C36D3927EADE43FE3890DB99B337CDB1E2A84047CE916E9BE1E291586740B7B58CB4C925C2ADE17E426ABAA829B655&open_id=73b24f53ffc64486eb40d606456fb04d&open_key=7386072b1f94fdd7acaae83cd0f0f1c1&sign_type=RSA×tamp=1514951422";
            /**
             * 3 RSA加密(小写),二进制转十六进制
             */
            sign = TLinxRSACoder.sign(sortStr.getBytes("utf-8"), TestParams.PRIVATE_KEY);
            System.out.println("sign:"+sign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }
    /**
     * 交易验签
     * @param respObject
     * @return
     */
    public static Boolean verifySign(JSONObject respObject) {
        String respSign = respObject.get("sign").toString();

        respObject.remove("sign");    // 删除sign节点
        respObject.put("open_key", TestParams.OPEN_KEY); // 添加open_key
     // 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
        String veriSign = sign(respObject);    

        if (respSign.equals(veriSign)) {
            System.out.println("=====验签成功=====");

            return true;
        } else {
            System.out.println("=====验签失败=====");
        }

        return false;
    }

    /**
     * AES加密，再二进制转十六进制(bin2hex)
     * @param postmap 说明：
     * @throws Exception
     */
    public static void handleEncrypt(TreeMap<String, Object> datamap, TreeMap<String, String> postmap) throws Exception {

        JSONObject dataobj = JSONObject.fromObject(datamap);
        String data    = TLinxAESCoder.encrypt(dataobj.toString(), TestParams.OPEN_KEY);    // AES加密，并bin2hex
        System.out.println("---AES加密后的data数据---"+data);
        postmap.put("data", data);
        System.out.println("--postmap1--"+postmap);
    }

    /**
     * 签名
     * @param postmap
     */
    public static void handleSign(TreeMap<String, String> postmap) {
        Map<String, String> veriDataMap = new HashMap<String, String>();

        veriDataMap.putAll(postmap);
        veriDataMap.put("open_key", TestParams.OPEN_KEY);
        System.out.println("--将要签名的数据--"+veriDataMap);
        // 签名
        String sign = sign(veriDataMap);

        postmap.put("sign", sign);
        System.out.println("--postmap2--"+postmap);
    }
    
    /**
     * 签名
     * @param postmap
     */
    public static void handleSignRSA(TreeMap<String, String> postmap) {
        Map<String, String> veriDataMap = new HashMap<String, String>();

        veriDataMap.putAll(postmap);
        veriDataMap.put("open_key", TestParams.OPEN_KEY);

        // 签名
        String sign = signRSA(veriDataMap);

        postmap.put("sign", sign);
        System.out.println("请求postmap---"+postmap);
    }
    

    /**
     * 请求接口
     * @param postmap
     * @return      响应字符串
     */
    public static String handlePost(TreeMap<String, String> postmap, String interfaceName) {
        String url = TestParams.OPEN_URL + interfaceName;
        System.out.println("url----:"+url);
        if (url.contains("https")) {
            return HttpsUtil.httpMethodPost(url, postmap, "UTF-8");
        } else {
            return HttpUtil.httpMethodPost(url, postmap, "UTF-8");
        }
    }

    /**
     * Method main
     * Description 说明：
     *
     * @param args 说明：
     */
    public static void main(String[] args) {

        // 初始化参数
        String pmtType   = "0,1,2,3";
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
			handleEncrypt(datamap, postmap);

            /**
             * 2 请求参数签名 按A~z排序，串联成字符串，先进行sha1加密(小写)，再进行md5加密(小写)，得到签名
             */
            handleSign(postmap);

            /**
             * 3 请求、响应
             */
            String rspStr = handlePost(postmap, TestParams.PAYLIST);


            /**
             * 4 验签  有data节点时才验签
             */
            JSONObject respObject = JSONObject.fromObject(rspStr);

            Object dataStr    = respObject.get("data");
			System.out.println("返回data字符串="+dataStr);

            if (!rspStr.isEmpty() || (dataStr != null)) {
                if (verifySign(respObject)) {    // 验签成功

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
