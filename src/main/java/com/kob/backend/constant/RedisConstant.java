package com.kob.backend.constant;

/**
 * @Author: shiyong
 * @CreateTime: 2024-11-02
 * @Description: Redis 常量
 */
public interface RedisConstant {
	/**
	 * 用户签到记录的Redis key 前缀
	 */
  String USER_SIGN_IN_REDIS_KEY_PREFIX = "user.signins";

	/**
	 * 获取用户签到记录的Redis key
	 * @param year 年份
	 * @param userId 用户 id
	 * @return 拼接好的Redis Key
	 */
  static String getUserSignInRedisKeyPrefix(int year, long userId) {
  	return String.format("%s:%s:%S",USER_SIGN_IN_REDIS_KEY_PREFIX,year,userId);
  }

}
