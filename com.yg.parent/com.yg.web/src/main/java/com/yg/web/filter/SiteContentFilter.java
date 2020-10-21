package com.yg.web.filter;


import com.yg.web.entity.SiteContext;
import com.yg.web.entity.SiteContextThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(2)
@WebFilter(filterName = "siteContentFilter", urlPatterns = "/*")
public class SiteContentFilter implements Filter {
    private final static Logger LOG = LoggerFactory.getLogger(SiteContentFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //初始化
        SiteContext context = new SiteContext();
        context.init(req, res);
        SiteContextThreadLocal.set(context);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
