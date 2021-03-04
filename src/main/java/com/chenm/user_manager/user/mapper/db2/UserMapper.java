package com.chenm.user_manager.user.mapper.db2;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.chenm.user_manager.user.model.dto.UserDTO;
import com.chenm.user_manager.user.model.entity.User;
import com.chenm.user_manager.user.model.req.UserListReq;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface UserMapper {
	
	/**
	 * 添加用户
	 * @param user
	 * @return
	 */
	public int addUser(User user);
	
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(@Param("loginName") String loginName);

	/**
	 * 修改用户信息
	 * @param user
	 */
	public void updateUser(User user);

	/**
	 * 用户列表
	 * @param req
	 * @return
	 */
	public List<UserDTO> listUser(UserListReq req);

	/**
	 * 获取用户详情
	 * @param userId
	 * @return
	 */
	public User getUserById(String userId);
	
}
