package com.chenm.user_manager.user.model.req;

import com.chenm.user_manager.common.model.PageReq;

public class UserListReq extends PageReq{
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
