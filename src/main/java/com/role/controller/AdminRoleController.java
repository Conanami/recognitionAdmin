package com.role.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybatis.one.po.DBAdminRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.exception.ExceptionFormatter;
import com.common.util.WSResponse;
import com.role.service.IAdminMenuRoleService;
import com.role.service.IAdminUserRoleService;

@Controller
public class AdminRoleController {
	private static Logger log = LoggerFactory.getLogger(AdminRoleController.class);
	
	@Resource
	IAdminMenuRoleService viewMenuRoleService;
	@Resource
	IAdminUserRoleService adminUserRoleService; 
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		log.error("Exception异常：" + ex);
		ExceptionFormatter.setResponse(response, ex);
		return response;
	}
	
	/**
	 * 查询所有管理员角色
	 * @param servletResponse
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.role.query")
	@ResponseBody
	public WSResponse<DBAdminRole> role_query(
			HttpSession httpSession) throws Exception{
		WSResponse<DBAdminRole> response = new WSResponse<DBAdminRole>();
		response.addAll(adminUserRoleService.queryadminrole());
		response.setRespDescription("查询所有管理员角色成功"); 
		return response;
	}
	
	/**
	 * 角色更新
	 * @param roleid
	 * @param rolename
	 * @param roledesc
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("admin.role.update")
	@ResponseBody
	public WSResponse<Boolean> role_update(
			Integer roleid, String rolename, String roledesc,
			HttpSession httpSession){
		adminUserRoleService.updateRole(roleid, rolename, roledesc);
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		response.add(true);
		if (roleid==null || roleid==0) {
			response.setRespDescription("新增角色成功");
		}else{
			response.setRespDescription("更新角色成功");
		}
		return response;
	}
	
	/**
	 * 删除指定角色
	 * @param roleid
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.role.remove")
	@ResponseBody
	public WSResponse<Boolean> role_remove(
			Integer roleid,
			HttpSession httpSession) throws Exception{
		adminUserRoleService.deleteRole(roleid);
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		response.add(true);
		response.setRespDescription("删除角色成功");		 
		return response;
	}
	
	/**
	 * 更新指定管理员对应的角色
	 * @param userid
	 * @param roleid
	 * @param servletResponse
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.user.role.update")
	@ResponseBody
	public WSResponse<Boolean> user_role_update(
			@RequestParam(value = "userid", required = true) Integer userid,
			@RequestParam(value = "roleid", required = true) Integer roleid,
			HttpServletResponse servletResponse,
			HttpSession httpSession) throws Exception{
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		adminUserRoleService.updateUserRole(userid, roleid);
		response.add(true);
		response.setRespDescription("更新管理员角色成功"); 
		return response;
	}
}
