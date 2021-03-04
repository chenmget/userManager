package com.chenm.user_manager.user.service;

import com.chenm.user_manager.common.model.ResultModel;
import com.chenm.user_manager.user.model.req.UserAddReq;
import com.chenm.user_manager.user.model.req.UserDeleteReq;
import com.chenm.user_manager.user.model.req.UserListReq;
import com.chenm.user_manager.user.model.req.UserLoginReq;
import com.chenm.user_manager.user.model.req.UserUpdateReq;

/**
 * 用户接口
 * @author admin
 *
 */
public interface UserService {
	/**
	 * 添加用户
	 * @param req
	 * @return
	 */
	public ResultModel addUser(UserAddReq req);
	
	/**
	 * 用户登录
	 * @param req
	 * @return
	 */
	public ResultModel login(UserLoginReq req);
	
	/**
	 * 编辑用户
	 * @param req
	 * @return
	 */
	public ResultModel updateUser(UserUpdateReq req);
	
	/**
	 * 删除用户
	 * @param req
	 * @return
	 */
	public ResultModel deleteUser(UserDeleteReq req);
	
	/**
	 * 用户列表
	 * @param req
	 * @return
	 */
	public ResultModel listUser(UserListReq req);
	
	/**
	 * 用户详情
	 * @param userId
	 * @return
	 */
	public ResultModel getUser(String userId);
}
