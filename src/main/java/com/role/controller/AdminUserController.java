package com.role.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.exception.ExceptionConst;
import com.common.exception.ExceptionFormatter;
import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.WSResponse;
import com.role.mybatis.mapper.CAdminRoleMapper;
import com.role.service.IAdminUserRoleService;
import com.role.service.IAdminUserService;

@Controller
public class AdminUserController {
	private static Logger log = LoggerFactory.getLogger(AdminUserController.class);
	
	@Resource
	IAdminUserRoleService adminUserRoleService;
	@Resource
	CAdminRoleMapper cadminUserService;
	@Resource
	IAdminUserService adminUserService;
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		log.error("Exception异常：" + ex);
		ExceptionFormatter.setResponse(response, ex);
		return response;
	}
	
	/**
	 * 查询所有管理用户
	 * @param servletResponse
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.user.query")
	@ResponseBody
	public WSResponse<HashMap<String, ?>> user_query(
			HttpSession httpSession) throws Exception{
		WSResponse<HashMap<String, ?>> response = new WSResponse<HashMap<String, ?>>();
		response.addAll(cadminUserService.selectAdminUsers());
		response.setRespDescription("查询所有用户成功"); 
		return response;
	}
	
	/**
	 * 新增用户
	 * @param username
	 * @param realname
	 * @param department
	 * @param mobile
	 * @param httpSession
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("admin.user.add")
	@ResponseBody
	public WSResponse<Boolean> addNewUser(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "realname", required = false) String realname,
			@RequestParam(value = "department", required = false) String department,
			@RequestParam(value = "mobile", required = false) String mobile,
			HttpSession httpSession) throws Exception{
		if (IopUtils.isEmpty(username)) {
			throw new WException(ExceptionConst.INPUT_VALID_UserID.intValue());
		}
		adminUserService.insertAddUser(username, realname, department, mobile);
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		response.add(true);
		response.setRespDescription("增加管理员用户成功");
		return response;
	}
	
	/**
	 * 删除指定管理员用户
	 * @param userid
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.user.remove")
	@ResponseBody
	public WSResponse<Boolean> removeNewUser(
			@RequestParam(value = "userid", required = true) Integer userid,
			HttpSession httpSession) throws Exception{
		if (userid==null) {
			throw new WException(ExceptionConst.INPUT_VALID_UserID.intValue());
		}
		adminUserService.deleteUser(userid);
		adminUserRoleService.deleteUserRole(userid);
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		response.add(true);
		response.setRespDescription("删除管理员用户成功");
		return response;
	}
	
	/**
	 * 更新管理员密码
	 * @param username
	 * @param password
	 * @param servletResponse
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.user.updatepwd")
	@ResponseBody
	public WSResponse<Boolean> updatepwd(
			@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password,
			HttpSession httpSession) throws Exception{
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		adminUserService.updateAdminUserPwd(username, password);
		response.add(true);
		response.setRespDescription("更新密码成功"); 
		return response;
	}
}
