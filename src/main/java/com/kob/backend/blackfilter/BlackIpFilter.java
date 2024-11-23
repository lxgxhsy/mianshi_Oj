package com.kob.backend.blackfilter;

import com.kob.backend.utils.NetUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: shiyong
 * @CreateTime: 2024-11-23
 * @Description:
 * @Version: 1.0
 */


@WebFilter(urlPatterns = "/*", filterName = "blackIpFilter")
public class BlackIpFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

		String ipAddress = NetUtils.getIpAddress((HttpServletRequest) servletRequest);
		if (BlackIpUtils.isBlackIp(ipAddress)) {
			servletResponse.setContentType("text/json;charset=UTF-8");
			servletResponse.getWriter().write("{\"errorCode\":\"-1\",\"errorMsg\":\"黑名单IP，禁止访问\"}");
			return;
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

}

