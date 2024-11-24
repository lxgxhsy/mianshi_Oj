package com.kob.backend.blackfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * @Author: sy
 * @CreateTime: 2024-11-23
 * @Description: 黑名单过滤工具类
 * @Version: 1.0
 */


@Slf4j
public class BlackIpUtils {

	public static BitMapBloomFilter bloomFilter;

	/**
	 * 判断 ip 是否在黑名单中
	 */
	public static boolean isBlackIp(String ip) {

		return bloomFilter.contains(ip);
	}

	/**
	 * 重建 ip 黑名单
	 * @Param configInfo
	 */
	public static void rebuildBlackIp(String configInfo) {
		log.info("{}",configInfo);
          if(StrUtil.isBlank(configInfo)){
          	configInfo = "{}";
          }
		/**
		 * 解析yaml文件
		 */
		Yaml yaml = new Yaml();
        Map map =  yaml.loadAs(configInfo, Map.class);
        List<String> blackIpList = (List<String>)map.get("blackIpList");
        log.info("blackIpList:{}",blackIpList);
        //加锁防止并发
         synchronized (BlackIpUtils.class){
	         if(CollUtil.isNotEmpty(blackIpList)){
		         //注意构造参数的设置
		         BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(958506);
		         for (String blackIp : blackIpList) {
			         bitMapBloomFilter.add(blackIp);
		         }
		         bloomFilter = bitMapBloomFilter;
	         }else{
		         bloomFilter = new BitMapBloomFilter(100);
	         }
         }
	}

}
