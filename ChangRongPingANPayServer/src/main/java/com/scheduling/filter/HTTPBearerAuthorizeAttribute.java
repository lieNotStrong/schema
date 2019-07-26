package com.scheduling.filter;

import com.alibaba.fastjson.JSON;
import com.scheduling.common.util.Result;
import com.scheduling.config.JwtFactory;
import com.scheduling.config.TokenCacheUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("rawtypes")
@Configuration
//@RefreshScope
public class HTTPBearerAuthorizeAttribute implements Filter {
    @Autowired
    private TokenCacheUtil tokenCacheUtil;

    Logger log = LoggerFactory.getLogger(HTTPBearerAuthorizeAttribute.class);

    // 请求白名单
    private String[] whiteList = new String[]{
            "hello",
            "login",
            ".js",
            ".css",
            ".gif",
            ".jpg",
            ".otf",
            ".png",
            ".ttf",
            ".apk",
            ".APK",
            ".html",
            ".zip",
            ".swf",
            ".xml",
            ".docx",
            ".json",
            ".doc",
            ".pdf",
            ".xls",
            ".ico",
            "v2",
            "layui",
            ".xlsx"

    };

    // 冻结状态白名单
    private String[] frozenWhiteList = new String[]{
            "/findPass"
    };
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO Auto-generated method stub
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Result resultMsg = new Result();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");

        // 处理跨域请求，仅用于开发环境，发布时，将此行代码注释
        /*if (debug) {
            cros(httpResponse);
        }

        if ("dev".equalsIgnoreCase(filterModel)) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }*/

        // 进行白名单过滤，白名单之内的请求，直接放行
        String uri = httpRequest.getRequestURI();
        System.out.println(uri);
        for (int i = 0; i < whiteList.length; i++) {
            if (uri.indexOf(whiteList[i]) == -1) {
                chain.doFilter(httpRequest, httpResponse);
                return;
            }
        }

        //String userId = getLoginUserId(httpRequest);
        //if (StringUtils.isNotBlank(userId)) {
        //httpRequest.setAttribute("userid", userId);
        // 获取用户信息
           /* CustomerUser cuser = customerUserIfac.getCustomerById(Integer.valueOf(userId)).getModel();
            if (cuser == null) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resultMsg.setResultCode(ResultCode.NOPERM);
                String jsonResult = JSON.toJSONString(resultMsg);
                httpResponse.getWriter().write(jsonResult);
                return;
            }*/
            /*// 如果账户被冻结，则返回“10000”消息
            if (cuser.getIsfrozen()) {
                // 进行冻结白名单过来，直接放行
                for (String s : frozenWhiteList) {
                    if (uri.contains(s)) {
                        chain.doFilter(httpRequest, httpResponse);
                        return;
                    }
                }
                resultMsg.setResultCode(ResultCode.ACCOUNT_FROZEN);
                String jsonResult = JSON.toJSONString(resultMsg);
                httpResponse.getWriter().write(jsonResult);
                return;
            }*/
        //chain.doFilter(httpRequest, httpResponse);
        //return;
        // }
        String skey = request.getParameter("skey");
        String openid = tokenCacheUtil.getOpenidstr(skey);
        if(null!=openid&&!"-1".equals(openid)){
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resultMsg.setMsg_cn("没有权限");
        String jsonResult = JSON.toJSONString(resultMsg);
        httpResponse.getWriter().write(jsonResult);
        return;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    private String getLoginUserId(HttpServletRequest request) {
        String userId = null;
        String auth = request.getHeader("Authorization");

        if ((auth != null) && (auth.length() > 7)) {
            Claims claims = JwtFactory.parseJWT(auth);
            if (claims != null) {
                userId = claims.get("userid") == null ? "" : claims.get("userid").toString();
                if (!tokenCacheUtil.checkTokenExists(auth, userId)) {
                    return null;
                }

                request.setAttribute("userid", userId);
            }
        }
        return userId;
    }

    /**
     * 处理跨域请求
     *
     * @param response
     */
    private void cros(HttpServletResponse response) {
        //这里填写你允许进行跨域的主机ip
        response.setHeader("Access-Control-Allow-Origin", "*");
        //允许的访问方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        //Access-Control-Max-Age 用于 CORS 相关配置的缓存
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, from");
    }
}
