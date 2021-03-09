package com.chenm.user_manager.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chenm.user_manager.common.model.ResultModel;
import com.chenm.user_manager.user.model.req.UserAddReq;
import com.chenm.user_manager.user.model.req.UserDeleteReq;
import com.chenm.user_manager.user.model.req.UserListReq;
import com.chenm.user_manager.user.model.req.UserLoginReq;
import com.chenm.user_manager.user.model.req.UserUpdateReq;
import com.chenm.user_manager.user.service.UserService;


/**
 * 用户控制层
 * @author admin
 *
 */
@RestController
@RequestMapping("api/user")
public class UserController {
	 @Autowired
	 private UserService userService;
	 
	
	
	 /**
	  * 用户注册
	  * @param userAddReq
	  * @param req
	  * @return
	  */
    @RequestMapping(value="/addUser",method = RequestMethod.POST)
    public ResultModel addUser(@RequestBody UserAddReq userAddReq,HttpServletRequest request ){
        return userService.addUser(userAddReq);
    }
    
    /**
     * 用户登录
     * @param req
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public ResultModel login(@RequestBody UserLoginReq req){
        return userService.login(req);
    }
    
   
    
    /**
     * 修改用户
     * @param req
     * @return
     */
    @RequestMapping(value="/updateUser",method = RequestMethod.POST)
    public ResultModel updateUser(@RequestBody UserUpdateReq req,HttpServletRequest request){
    	String updateId = getUserIdFromReq(request);
    	req.setUpdateId(updateId);
        return userService.updateUser(req);
    }
    
    /**
     * 修改密码
     * @param req
     * @return
     */
    @RequestMapping(value="/updatePsw",method = RequestMethod.POST)
    public ResultModel updatePsw(@RequestBody UserUpdateReq req,HttpServletRequest request){
    	String updateId = getUserIdFromReq(request);
    	req.setUpdateId(updateId);
        return userService.updatePsw(req);
    }
    
    

	/**
     * 删除用户
     * @param req
     * @return
     */
    @RequestMapping(value="/deleteUser/{userId}",method = RequestMethod.DELETE)
    public ResultModel deleteUser(@PathVariable("userId") String userId){
   
        return userService.deleteUser(userId);
    }
    
    /**
     * 用户列表
     * @param req
     * @return
     */
    @RequestMapping(value="/listUser",method = RequestMethod.POST)
    public ResultModel listUser(@RequestBody UserListReq req){
        return userService.listUser(req);
    }
    
    /**
     * 用户详情
     * @param userId
     * @return
     */
    @RequestMapping(value="/getUser",method = RequestMethod.GET)
    public ResultModel getUser(String userId){
        return userService.getUser(userId);
    }
    
    /**
     * 伪代码，获取登录用户信息
     * @param request
     * @return
     */
    private String getUserIdFromReq(HttpServletRequest request) {
		return "1";
	}

}
