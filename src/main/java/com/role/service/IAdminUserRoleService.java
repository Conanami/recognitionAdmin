package com.role.service;

import java.util.List;

import mybatis.one.po.DBAdminRole;
import mybatis.one.po.DBAdminUserRole;

import com.role.controller.RoleUserDto;

public interface IAdminUserRoleService {
    /**
     * 根据userid获取该user所有的角色
     * @param userid
     * @return
     */
	public List<DBAdminUserRole> queryByUserid(Integer userid);
	
	/**
	 * 根据用户名查询用户的所有角色信息
	 * @param username
	 * @return
	 */
	public List<RoleUserDto> queryRoleWithUsername(String username);
	
	/**
	 * 查询所有的管理员角色
	 * @return
	 */
	public List<DBAdminRole> queryadminrole();
	
	/**
	 * 更新角色基本信息
	 * @param roleid
	 * @param rolename
	 * @param roledesc
	 */
	public void updateRole(Integer roleid, String rolename, String roledesc);
	
	/**
	 * 删除指定角色
	 * @param roleid
	 * @throws Exception
	 */
	public void deleteRole(Integer roleid) throws Exception;
	
	/**
	 * 更新指定用户的角色
	 * @param userid
	 * @param roleid
	 * @throws Exception
	 */
	public void updateUserRole(Integer userid, Integer roleid) throws Exception;
	
	/**
	 * 删除人员角色表里面 指定用户的所有信息
	 * @param userid
	 * @throws Exception
	 */
	public void deleteUserRole(Integer userid) throws Exception;
	
}
