package com.chenm.user_manager.common.util;

/**
 * redis 工具类
 */
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	
	Logger logger =LoggerFactory.getLogger(RedisUtil.class);
	
	public static final String USER_REDIS="1";
	public static final String ORDER_REDIS="2";
	
	@Resource(name = "redisOrderTemplate")
    StringRedisTemplate redisOrderTemplate;
 
    @Resource(name = "redisUserTemplate")
    StringRedisTemplate redisUserTemplate;
	
    /**
     * 获取redis源
     * @param type
     * @return
     */
	public StringRedisTemplate getRedisTemp(String type) {
		if(USER_REDIS.equals(type)) {
			return redisUserTemplate;
		}else if(ORDER_REDIS.equals(type)) {
			return redisOrderTemplate;
		}
		return null;
	}
	
	
	public void setObject(String key, Object obj , String source) {
		if(obj==null) {
			return;
		}
		StringRedisTemplate template = getRedisTemp(source);
		if(template==null) {
			throw new RuntimeException("未找到数据源");
		}
		Map<String, String> map;
		try {
			map = BeanUtils.describe(obj);
			for(Map.Entry<String, String> entry : map.entrySet()) {
				if(entry.getValue()!=null) {
					template.opsForHash().put(key, entry.getKey(), entry.getValue());
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			logger.error("对象转换异常");
		}
		
	}
	
	public Map<Object, Object> getHashObject(String key, String source) {
	
		StringRedisTemplate template = getRedisTemp(source);
		if(template==null) {
			throw new RuntimeException("未找到数据源");
		}
		Map<Object, Object> map = template.opsForHash().entries(key);
		
		return map;
		
	}

}
