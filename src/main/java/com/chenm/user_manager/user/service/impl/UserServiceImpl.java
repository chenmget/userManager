package com.chenm.user_manager.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.chenm.user_manager.common.constant.CommonConstant;
import com.chenm.user_manager.common.constant.RedisPrefixConstant;
import com.chenm.user_manager.common.model.ResultModel;
import com.chenm.user_manager.common.util.DesUtil;
import com.chenm.user_manager.common.util.IdUtil;
import com.chenm.user_manager.common.util.RedisUtil;
import com.chenm.user_manager.user.mapper.db1.OrderMapper;
import com.chenm.user_manager.user.mapper.db2.UserMapper;
import com.chenm.user_manager.user.model.dto.UserDTO;
import com.chenm.user_manager.user.model.entity.User;
import com.chenm.user_manager.user.model.req.UserAddReq;
import com.chenm.user_manager.user.model.req.UserDeleteReq;
import com.chenm.user_manager.user.model.req.UserListReq;
import com.chenm.user_manager.user.model.req.UserLoginReq;
import com.chenm.user_manager.user.model.req.UserUpdateReq;
import com.chenm.user_manager.user.model.resp.UserLoginResp;
import com.chenm.user_manager.user.model.resp.UserResp;
import com.chenm.user_manager.user.service.UserService;
import com.chenm.user_manager.user.util.ValidityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserServiceImpl implements UserService{
	Logger logger =LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserMapper usermapper;
	@Autowired
	private OrderMapper orderMapper;
	
	
	@Autowired
	private RedisUtil redisUtil;
	
	private static final String INIT_PASSWORD="123456Aa";//默认密码
	private static final int MAX_LOGIN_TIME=3;//最大允许登录次数
	
	@Override
	public ResultModel addUser(UserAddReq req) {
		logger.info("UserServiceImpl.addUser req={}",JSON.toJSON(req)); 
		//校验登录名是否存在
		String loginName = req.getLoginName();
		if(StringUtils.isEmpty(loginName)) {
			return ResultModel.error("登录名不能为空");
		}
		User user = usermapper.getUserByLoginName(loginName);
		if(user!=null) {
			return ResultModel.error("注册账号已存在");
		}
		user = new User();
		BeanUtils.copyProperties(req, user);
		//主键赋值
		String userId = IdUtil.getUUID();
		user.setUserId(userId);
		//设置默认密码
		user.setPassword(DigestUtils.md5DigestAsHex(INIT_PASSWORD.getBytes()));
		user.setCreateTime(new Date());
		user.setState(CommonConstant.VALID);
		//数据入库
		int i  = usermapper.addUser(user);
		//测试多数据源
		orderMapper.addUser(user);
		if(i>0) {
			//添加缓存
			UserDTO dto = new UserDTO();
			BeanUtils.copyProperties(user, dto);
			//测试多redis源
			redisUtil.setObject(RedisPrefixConstant.USER+loginName, dto, RedisUtil.USER_REDIS);
			redisUtil.setObject(RedisPrefixConstant.USER+userId, dto, RedisUtil.USER_REDIS);
			redisUtil.setObject(RedisPrefixConstant.ORDER+loginName, dto, RedisUtil.ORDER_REDIS);
			return ResultModel.ok("注册成功");
		}
		return ResultModel.error("注册失败");
	}

	@Override
	public ResultModel login(UserLoginReq req) {
		logger.info("UserServiceImpl.login req={}",JSON.toJSON(req)); 
		String loginName = req.getLoginName();
		//前端加密密文
		String encryPassword = req.getPassword();
		if(loginName==null) {
			return ResultModel.error("登录账号不能为空");
		}
		if(encryPassword==null) {
			return ResultModel.error("密码不能为空");
		}
		//获取已登录次数
		String loginTime = redisUtil.getString(RedisPrefixConstant.USER_LOGIN_TIME+loginName, RedisUtil.USER_REDIS);
		if(loginTime==null) {
			loginTime="0";
		}
		int loginTimeInt = Integer.parseInt(loginTime);
		if(loginTimeInt>=MAX_LOGIN_TIME) {
			return ResultModel.error("密码输入次数过多，请10分钟后再试");
		}
		//解密后密码
		String decryPassword=null;
		try {
			decryPassword = DesUtil.decrypt(encryPassword);
		} catch (Exception e) {
			logger.error("用户注册解密失败，密码="+encryPassword);
			redisUtil.setString(RedisPrefixConstant.USER_LOGIN_TIME+loginName, String.valueOf(loginTimeInt+1), 10*60L,  RedisUtil.USER_REDIS);
			return ResultModel.error("用户密码传输有误");
		}
		//md5加密
		String md5Psw= DigestUtils.md5DigestAsHex(decryPassword.getBytes());
		
		
		//缓存中获取用户信息
		Map<Object, Object> userMap = redisUtil.getHashObject(RedisPrefixConstant.USER+loginName, RedisUtil.USER_REDIS);
		User user = null;
		if(userMap==null) {
			//查询数据库
			user = usermapper.getUserByLoginName(loginName);
		}else {
			user = JSON.parseObject(JSON.toJSONString(userMap), User.class);
		}
		if(user==null) {
			return ResultModel.error("该用户存在");
		}
		//校验密码一致
		if(!user.getPassword().equals(md5Psw)) {
			//缓存次数+1
			redisUtil.setString(RedisPrefixConstant.USER_LOGIN_TIME+loginName, String.valueOf(loginTimeInt+1), 10*60L,  RedisUtil.USER_REDIS);
			return ResultModel.error("登录账号密码不一致");
		}else {
			redisUtil.delete(RedisPrefixConstant.USER_LOGIN_TIME+loginName, RedisUtil.USER_REDIS);
		}
		UserLoginResp resp = new UserLoginResp();
		BeanUtils.copyProperties(user, resp);
		logger.info("UserServiceImpl.login req={}",JSON.toJSON(resp)); 
		return ResultModel.ok(resp);
	}

	@Override
	public ResultModel updateUser(UserUpdateReq req) {
		logger.info("UserServiceImpl.updateUser req={}",JSON.toJSON(req)); 
		String userId = req.getUserId();
		if(userId==null) {
			return ResultModel.error("用户id不能为空");
		}
		//更新密码
		if(req.getPassword()!=null) {
			String encryPassword = req.getPassword();
			//解密后密码
			String decryPassword=null;
			try {
				decryPassword = DesUtil.decrypt(encryPassword);
			} catch (Exception e) {
				logger.error("用户注册解密失败，密码="+encryPassword);
				return ResultModel.error("用户密码传输有误");
			}
			//校验密码合法性
			ResultModel rst = ValidityUtil.checkPasswordRule(decryPassword);
			if(!rst.isSuccess()) {
				return rst;
			}
			//md5加密
			String md5Psw= DigestUtils.md5DigestAsHex(decryPassword.getBytes());
			req.setPassword(md5Psw);
		}
		//缓存中获取用户信息
		Map<Object, Object> userMap = redisUtil.getHashObject(RedisPrefixConstant.USER+userId, RedisUtil.USER_REDIS);
		User user = null;
		if(userMap==null) {
			//查询数据库
			user = usermapper.getUserById(userId);
		}else {
			user = JSON.parseObject(JSON.toJSONString(userMap), User.class);
		}
		if(user==null) {
			return ResultModel.error("未找到相关用户");
		}
		BeanUtils.copyProperties(req, user);
		user.setUpdateTime(new Date());
		usermapper.updateUser(user);
		//添加缓存
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(user, dto);
		redisUtil.setObject(RedisPrefixConstant.USER+userId, dto, RedisUtil.USER_REDIS);
		return ResultModel.ok("修改成功");
	}

	@Override
	public ResultModel deleteUser(UserDeleteReq req) {
		logger.info("UserServiceImpl.deleteUser req={}",JSON.toJSON(req)); 
		String userId = req.getUserId();
		if(userId==null) {
			return ResultModel.error("用户id不能为空");
		}
		User user = new User();
		user.setUserId(req.getUserId());
		user.setUpdateId(req.getUpdateId());
		user.setUpdateTime(new Date());
		user.setState(CommonConstant.INVALID);
		usermapper.updateUser(user);
		//清空缓存数据
		redisUtil.delete(RedisPrefixConstant.USER+userId, RedisUtil.USER_REDIS);
		return ResultModel.ok("删除成功");
	}

	@Override
	public ResultModel listUser(UserListReq req) {
		logger.info("UserServiceImpl.listUser req={}",JSON.toJSON(req)); 
		PageHelper.startPage(req.getPageNum(), req.getPageSize());
		List<UserDTO> list = usermapper.listUser(req);
		PageInfo<UserDTO> page= new PageInfo<UserDTO>(list);
		return ResultModel.ok(page);
	}

	@Override
	public ResultModel getUser(String userId) {
		logger.info("UserServiceImpl.getUser req={}",JSON.toJSON(userId)); 
		if(userId==null) {
			return ResultModel.error("用户id不能为空");
		}
		User user = usermapper.getUserById(userId);
		if(user==null) {
			return ResultModel.error("未找到相关用户");
		}
		UserResp resp = new UserResp();
		BeanUtils.copyProperties(user, resp);
		return  ResultModel.ok(resp );
	}

}
 