package com.scheduling.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scheduling.common.util.WebUtil;
import com.scheduling.common.util.wechat.WechatApi;
import com.scheduling.common.util.wechat.WechatConf;
import com.scheduling.common.util.wechat.WechatTemplate;
import com.scheduling.common.util.wechat.WechatTemplateItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author wwz
 * @version 1 (2018/8/16)
 * @since Java7
 */
@Slf4j
@RestController
@RequestMapping("/wx_small_app")
public class WechatController {

    @GetMapping("/get_openid_by_js_code")
    public Map<String, Object> getOpenIdByJSCode(String js_code) {
        return WechatApi.getOpenIdByJSCode(js_code);
    }

    @PostMapping("/template_send")
    public Map<String, Object> templateSend() {
        String accessToken = WechatApi.getAccessToken();
        JSONObject body = JSON.parseObject(WebUtil.getBody());

        // 填充模板数据 （测试代码，写死）
        Map map= MapUtil.newHashMap();
        map.put("keyword1",new WechatTemplateItem(RandomUtil.randomString(10)));
        map.put("keyword2", new WechatTemplateItem(DateUtil.now()));
        map.put("keyword3", new WechatTemplateItem(RandomUtil.randomString(10)));//
        //map.put("keyword4", new WechatTemplateItem(RandomUtil.randomNumbers(10)));
        WechatTemplate wechatTemplate = new WechatTemplate()
                .setTouser(/*body.getString("openId")*/"oSIGH5KVEpqjwAXl4cWOqCnRtEFY")
                .setTemplate_id(WechatConf.templateId)
                // 表单提交场景下为formid，支付场景下为prepay_id
                .setForm_id(/*body.getString("formId")*/"0d7dab66992d4c208d90e79b3a13b53d")
                // 跳转页面
                //.setPage("pages/index")
                /**
                 * 模板内容填充：随机字符
                 * 购买地点 {{keyword1.DATA}}
                 * 购买时间 {{keyword2.DATA}}
                 * 物品名称 {{keyword3.DATA}}
                 * 交易单号 {{keyword4.DATA}}
                 * -> {"keyword1": {"value":"xxx"}, "keyword2": ...}
                 */
                .setData(map);
        String qq=WechatApi.templateSend(accessToken, wechatTemplate);
        map.put("body",qq);
        return map;
    }
}
