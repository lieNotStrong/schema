package com.scheduling.controller;

import com.scheduling.utils.TLinx2Util;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
/**
 * 回调
 * @author EX_KJKFB_LIUJIAJIA
 *
 */
@Controller
@RequestMapping("/callback")
public class TLinx2CallbackController{

    /**
     * Field LOGGER
     * Description
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TLinx2CallbackController.class);


    @RequestMapping(value = "/scanpay_cashier/payResult", method = RequestMethod.GET)
    @ResponseBody
    public String testPayCallback(HttpServletRequest request) {
        LOGGER.debug("接收到的支付回调信息为：{}", request);

        Map<String, String> params  = new TreeMap<String, String>();

        // 取出所有参数是为了验证签名
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();

            params.put(parameterName, request.getParameter(parameterName));
        }

        JSONObject paramsObject = JSONObject.fromObject(params);

        System.out.println(paramsObject.get("trade_result"));
        
        System.out.println("===========加密前paramsObject是：" + paramsObject.toString());
        //加密验签
        TLinx2Util.verifySign(paramsObject);
        System.out.println("===========回调参数是：" + paramsObject.toString());


        return "notify_success";
    }
}


