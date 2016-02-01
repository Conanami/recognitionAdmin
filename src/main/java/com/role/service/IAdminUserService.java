package com.role.service;

import mybatis.one.po.DBAdminUserInfo;

public interface IAdminUserService {
     
	/**
	 * 查询用户
	 * @param userName
	 * @param password
	 * @return
	 */
	public DBAdminUserInfo queryAdminUser(String userName);
	
	/**
	 * 更新指定用户的密码
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public void updateAdminUserPwd(String username, String password) throws Exception;
	
	/**
	 * 新增管理员用户
	 * @param username
	 * @param realname
	 * @param department
	 * @param mobile
	 * @throws Exception
	 */
	public void insertAddUser(String username, String realname, String department, String mobile) throws Exception;
	
	/**
	 * 删除指定用户
	 * @param userid
	 * @throws Exception
	 */
	public void deleteUser(Integer userid) throws Exception;
	
}
