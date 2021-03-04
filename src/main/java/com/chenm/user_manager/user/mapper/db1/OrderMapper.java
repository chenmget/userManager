package com.chenm.user_manager.user.mapper.db1;

import org.springframework.stereotype.Repository;

import com.chenm.user_manager.user.model.entity.User;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface OrderMapper {
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public int addUser(User user);
	
	
	
}
