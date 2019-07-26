package com.scheduling.filter;

import com.scheduling.config.JwtConstant;
import com.scheduling.config.JwtFactory;
import com.scheduling.config.TokenCacheUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class IsLoginFilter implements Filter {

    @Autowired
    private TokenCacheUtil tokenCacheUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }

    @Value("${token.expire}")
    public void setExpire(Long expire) {
        if (expire != null) {
            JwtConstant.JWT_REFRESH_TTL = Long.valueOf(expire);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String userId = getLoginUserId(request);
        if (StringUtils.isNotBlank(userId)) {
            response.setHeader("isLogin", userId);
        } else {
            response.setHeader("isLogin", "-1");
        }
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

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
}
