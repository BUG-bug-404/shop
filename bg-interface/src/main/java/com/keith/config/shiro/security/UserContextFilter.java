package com.keith.config.shiro.security;

import com.keith.common.SecurityConsts;
import com.keith.modules.entity.user.UserMember;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "userContextFilter",urlPatterns = "/*")
public class UserContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String authorization = httpServletRequest.getHeader(SecurityConsts.REQUEST_AUTH_HEADER);
        if(authorization!=null){
            String account = JwtUtil.getClaim(authorization, SecurityConsts.ACCOUNT);
            UserMember loginUser = new UserMember();
            loginUser.setMobile(account);
            try (UserContext context = new UserContext(loginUser)) {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }else{
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
