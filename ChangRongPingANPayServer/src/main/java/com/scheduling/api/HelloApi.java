package com.scheduling.api;

import com.scheduling.config.Base64;
import com.scheduling.config.JwtFactory;
import com.scheduling.config.TokenCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
//注意这里必须为Controller
public class HelloApi {
    @Autowired
    TokenCacheUtil tokenCacheUtil;
    /**
     * @return
     */
    @RequestMapping("/hello")
    public String helloHtm(HttpServletRequest request) {
        System.out.println(Base64.getCurrentLoginUserid(request));
        return "/index";
    }
    @RequestMapping("/hello1")
    @ResponseBody
    public Map helloHtml(HttpServletRequest request) {
        Map map = new HashMap();
        map.put("hello", "欢迎进入HTML页面");
        map.put("userid", "欢迎进入HTML页面");
        // 生成Token
        String token = JwtFactory.createJWT("user", "123456");
        tokenCacheUtil.saveToken(token, "123456");
        map.put("token", token);
        System.out.println(token);
        return map;
    }
}
