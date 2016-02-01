package com.role.mybatis.mapper;

import java.util.HashMap;
import java.util.List;

import com.role.controller.RoleMenuDto;
import com.role.controller.RolePermissionsDto;
import com.role.controller.RoleUserDto;

public interface CAdminRoleMapper {
	
	/**
	 * 查询所有的管理员用户
	 * @return
	 */
	List<HashMap<String, ?>> selectAdminUsers();
	
	/**
	 * 查询指定角色的 菜单
	 * @param roleid
	 * @return
	 */
	List<RoleMenuDto> selectAdminRoleMenu(Integer roleid);
	
	/**
	 * 查询所有的角色菜单以及权限,  若菜单有多个角色有权限的，使用逗号连接角色名字
	 * @return
	 */
	List<RolePermissionsDto> selectAllRoleMenuPermission();
	
	/**
	 * 根据用户名查询用户的所有角色信息
	 * @param username
	 * @return
	 */
	List<RoleUserDto> selectRoleWithUsername(String username);
}
