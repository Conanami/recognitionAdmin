package com.role.service;

import java.util.List;

import mybatis.one.po.DBAdminMenu;
import mybatis.one.po.DBAdminRoleMenu;

import com.common.util.WSResponse;
import com.role.controller.RoleMenuDto;
import com.role.controller.RolePermissionsDto;

public interface IAdminMenuRoleService {

	/**
	 * 根据userid获取用户相关的菜单
	 * @param userId
	 * @return
	 */
	public List<RoleMenuDto> queryMenuByRoleid(Integer roleid);
	
	/**
	 * 查询所有的角色菜单以及权限
	 * @return
	 */
	public List<RolePermissionsDto> selectAllRoleMenuPermission();
	
	/**
	 * 查询所有菜单
	 * @return
	 */
	public WSResponse<DBAdminMenu> queryAllMenu(Integer page ,Integer pagesize);
	
	/**
	 * 新增或者更新指定菜单
	 * @param menuid
	 * @param menuname
	 * @param url
	 * @param type
	 * @param parentmenuid
	 * @param isleaf
	 * @throws Exception 
	 */
	public void updateMenu(Integer menuid, String menuname, String url, Integer type, Integer parentmenuid, Integer isleaf) throws Exception;
	
	/**
	 * 删除指定菜单
	 * @param menuid
	 */
	public void deleteMenu(Integer menuid);
	
	/**
	 * 查询角色的所属菜单
	 * @param roleid
	 * @return
	 */
	public List<RoleMenuDto> queryRoleMenu(Integer roleid);
	
	/**
	 * 更新指定角色的所属菜单
	 * @param roleid
	 * @param menuidlist
	 */
	public void updateRoleMenu(Integer roleid, List<DBAdminRoleMenu> menuidlist);

}
