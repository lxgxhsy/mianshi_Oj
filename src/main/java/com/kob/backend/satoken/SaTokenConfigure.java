package com.kob.backend.satoken;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: shiyong
 * @CreateTime: 2024-11-24
 * @Description: Sa-Token 全局拦截器 （为了支持鉴权）
 * @Version: 1.0
 */

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
	}
}
