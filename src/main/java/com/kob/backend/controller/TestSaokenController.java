package com.kob.backend.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: shiyong
 * @CreateTime: 2024-11-24
 * @Description: 测试Sa-Token测试
 * @Version: 1.0
 */

@RestController
@RequestMapping("/test/user/")
public class TestSaokenController {
	// 测试登录，浏览器访问： http://localhost:8101/api/test/user/doLogin?username=zhang&password=123456
	@RequestMapping("doLogin")
	public String doLogin(String username, String password) {
		// 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
		if("zhang".equals(username) && "123456".equals(password)) {
			StpUtil.login(10001);
			return "登录成功";
		}
		return "登录失败";
	}

	// 查询登录状态，浏览器访问： http://localhost:8101/api/test/user/isLogin
	@RequestMapping("isLogin")
	public String isLogin() {
		StpUtil.checkLogin();
		return "当前会话是否登录：" + StpUtil.isLogin();
	}
}
