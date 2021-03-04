package com.chenm.user_manager.user.model.entity;

import java.util.Date;

public class User {
	private String userId;//主键
	private String loginName;//登录名
	private String userName;//用户姓名
	private String password;//密码
	private String idCard;//身份证
	
	private Integer state;//是否有效 1有效2无效
	private Date createTime;//创建时间
	private Date updateTime;//修改时间
	private String crateId;//创建者id
	private String updateId;//修改者id
	
	
	
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCrateId() {
		return crateId;
	}
	public void setCrateId(String crateId) {
		this.crateId = crateId;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	
	
	
}
