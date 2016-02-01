package com.role.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import mybatis.one.mapper.DBAdminRoleMapper;
import mybatis.one.mapper.DBAdminUserInfoMapper;
import mybatis.one.mapper.DBAdminUserRoleMapper;
import mybatis.one.po.DBAdminRole;
import mybatis.one.po.DBAdminRoleExample;
import mybatis.one.po.DBAdminUserInfo;
import mybatis.one.po.DBAdminUserInfoExample;
import mybatis.one.po.DBAdminUserRole;
import mybatis.one.po.DBAdminUserRoleExample;

import org.springframework.stereotype.Service;

import com.common.exception.ExceptionConst;
import com.common.exception.WException;
import com.role.controller.RoleUserDto;
import com.role.mybatis.mapper.CAdminRoleMapper;
import com.role.service.IAdminUserRoleService;
/**
 * 根据userid获取该userid下的所有的roleid
 * @author Administrator
 *
 */
@Service
public class AdminUserRoleServiceImpl implements IAdminUserRoleService {
	@Resource
	private DBAdminUserRoleMapper adminUserRoleMapper;
	@Resource
	private DBAdminRoleMapper adminRoleMapper; 
	
	@Resource
	DBAdminUserInfoMapper adminUserMapper;
	@Resource
	CAdminRoleMapper cadminRoleMapper;

	@Override
	public List<DBAdminUserRole> queryByUserid(Integer userid) {
		DBAdminUserRoleExample ex = new DBAdminUserRoleExample();
		ex.createCriteria().andUseridEqualTo(userid);
		List<DBAdminUserRole> adminUserRole =  adminUserRoleMapper.selectByExample(ex);
		return adminUserRole;
	}
	
	/**
	 * 根据用户名查询用户的所有角色信息
	 * @param username
	 * @return
	 */
	public List<RoleUserDto> queryRoleWithUsername(String username){
		return cadminRoleMapper.selectRoleWithUsername(username);
	}
	
	/**
	 * 查询所有的管理员角色
	 * @return
	 */
	public List<DBAdminRole> queryadminrole(){
		DBAdminRoleExample ex = new DBAdminRoleExample();
		ex.createCriteria().andRoleidIsNotNull();
		List<DBAdminRole> list = adminRoleMapper.selectByExample(ex);
		return list;
	}
	
	/**
	 * 更新角色基本信息
	 * @param roleid
	 * @param rolename
	 * @param roledesc
	 */
	public void updateRole(Integer roleid, String rolename, String roledesc){
		DBAdminRole role = adminRoleMapper.selectByPrimaryKey(roleid);
		if (role==null) {
			role = new DBAdminRole();
			role.setRolename(rolename);
			role.setRoledesc(roledesc);
			adminRoleMapper.insert(role);
		}else{
			role.setRolename(rolename);
			role.setRoledesc(roledesc);
			adminRoleMapper.updateByPrimaryKey(role);
		}
	}
	
	/**
	 * 删除指定角色
	 * @param roleid
	 * @throws Exception
	 */
	public void deleteRole(Integer roleid) throws Exception{
		DBAdminRole role = adminRoleMapper.selectByPrimaryKey(roleid);
		if (role==null) {
			throw new WException(ExceptionConst.INPUT_VALID_ROLEID.intValue());
		}
		adminRoleMapper.deleteByPrimaryKey(roleid);
	}
	
	/**
	 * 更新指定用户的角色
	 * @param userid
	 * @param roleid
	 * @throws Exception
	 */
	public void updateUserRole(Integer userid, Integer roleid) throws Exception{
		{
			DBAdminUserInfoExample ex = new DBAdminUserInfoExample();
			ex.createCriteria().andUseridEqualTo(userid);
			List<DBAdminUserInfo> list = adminUserMapper.selectByExample(ex);
			if (list.size()==0) {
				throw new WException(ExceptionConst.INPUT_VALID_UserID.intValue());
			}
		}
		{
			DBAdminUserRoleExample ex = new DBAdminUserRoleExample();
			ex.createCriteria().andUseridEqualTo(userid);
			List<DBAdminUserRole> list = adminUserRoleMapper.selectByExample(ex);
			if (list.size()>0) {
				DBAdminUserRole user = list.get(0);
				user.setRoleid(roleid);
				adminUserRoleMapper.updateByPrimaryKey(user);
			}else{
				DBAdminUserRole user = new DBAdminUserRole();
				user.setUserid(userid);
				user.setRoleid(roleid);
				user.setCreatetime(new Date());
				adminUserRoleMapper.insert(user);
			}
		}
	}
	
	/**
	 * 删除人员角色表里面 指定用户的所有信息
	 * @param userid
	 * @throws Exception
	 */
	public void deleteUserRole(Integer userid) throws Exception{
		DBAdminUserRoleExample ex = new DBAdminUserRoleExample();
		ex.createCriteria().andUseridEqualTo(userid);
		adminUserRoleMapper.deleteByExample(ex);
	}
}
