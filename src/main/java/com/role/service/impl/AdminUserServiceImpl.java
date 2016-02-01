package com.role.service.impl;

import java.util.List;

import javax.annotation.Resource;

import mybatis.one.mapper.DBAdminUserInfoMapper;
import mybatis.one.po.DBAdminUserInfo;
import mybatis.one.po.DBAdminUserInfoExample;

import org.springframework.stereotype.Service;

import com.common.exception.ExceptionConst;
import com.common.exception.WException;
import com.common.util.MD5;
import com.role.service.IAdminUserService;
/**
 * 根据userid获取该userid下的所有的roleid
 * @author Administrator
 *
 */
@Service
public class AdminUserServiceImpl implements IAdminUserService {
	
	@Resource
	private DBAdminUserInfoMapper adminUserInfoMapper;
	
	/**
	 * 查询用户
	 * @param userName
	 * @param password
	 * @return
	 */
	@Override
	public DBAdminUserInfo queryAdminUser(String userName) {
		DBAdminUserInfoExample ex = new DBAdminUserInfoExample();
		ex.createCriteria().andUsernameEqualTo(userName);
		List<DBAdminUserInfo> adminUserInfoList = adminUserInfoMapper.selectByExample(ex);
		if (adminUserInfoList.size()==0) {
			return null;
		}
		return adminUserInfoList.get(0);
	}

	/**
	 * 新增管理员用户
	 * @param username
	 * @param realname
	 * @param department
	 * @param mobile
	 * @throws Exception
	 */
	public void insertAddUser(String username, String realname, String department, String mobile) throws Exception{
		DBAdminUserInfoExample ex = new DBAdminUserInfoExample();
		ex.createCriteria().andUsernameEqualTo(username);
		List<DBAdminUserInfo> list = adminUserInfoMapper.selectByExample(ex);
		if (list.size()>0) {
			throw new WException(ExceptionConst.INPUT_VALID_UserID_Exist.intValue());
		}
		DBAdminUserInfo user = new DBAdminUserInfo();
		user.setUsername(username);
		user.setRealname(realname);
		user.setDepartment(department);
		user.setMobile(mobile);
		adminUserInfoMapper.insert(user);
	}
	
	/**
	 * 删除指定用户
	 * @param userid
	 * @throws Exception
	 */
	public void deleteUser(Integer userid) throws Exception{
		DBAdminUserInfoExample ex = new DBAdminUserInfoExample();
		ex.createCriteria().andUseridEqualTo(userid);
		List<DBAdminUserInfo> list = adminUserInfoMapper.selectByExample(ex);
		if (list.size()==0) {
			throw new WException(ExceptionConst.INPUT_VALID_UserID.intValue());
		} 
		adminUserInfoMapper.deleteByPrimaryKey(userid);
	}
	
	/**
	 * 更新指定用户的密码
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception 
	 */
	public void updateAdminUserPwd(String userName, String password) throws Exception{
		DBAdminUserInfoExample ex = new DBAdminUserInfoExample();
		ex.createCriteria().andUsernameEqualTo(userName);
		List<DBAdminUserInfo> list = adminUserInfoMapper.selectByExample(ex);
		if (list.size()==0) {
			throw new WException(ExceptionConst.INPUT_VALID_UserID.intValue());
		}
		DBAdminUserInfo adminuser = list.get(0);
		adminuser.setPassword(MD5.encrypt(password));
		adminUserInfoMapper.updateByPrimaryKey(adminuser);
	}

}
