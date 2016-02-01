package com.role.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import mybatis.one.po.DBAdminMenu;
import mybatis.one.po.DBAdminRoleMenu;
import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.role.service.IAdminMenuRoleService;
import com.role.service.IAdminUserRoleService;
import com.role.shiro.ShiroAuthService;

@Controller
public class AdminMenuRoleController {
	private static Logger log = LoggerFactory.getLogger(AdminMenuRoleController.class);
	
	@Resource
	IAdminMenuRoleService adminMenuRoleService;
	@Resource
	IAdminUserRoleService adminUserRoleService; 
	@Autowired
	ShiroAuthService shiroServeice;
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		log.error("Exception异常：" + ex);
		ExceptionFormatter.setResponse(response, ex);
		return response;
	}
	
	/**
	 * 查询所有菜单
	 * @param servletResponse
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.menu.query")
	@ResponseBody
	public WSResponse<DBAdminMenu> menu_query(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pagesize", required = false) Integer pagesize,
			HttpSession httpSession) throws Exception{
		WSResponse<DBAdminMenu> response = adminMenuRoleService.queryAllMenu(page, pagesize);
		response.setRespDescription("查询所有菜单成功"); 
		return response;
	}
	
	/**
	 * 更新指定菜单
	 * @param menuid
	 * @param menuname
	 * @param url
	 * @param type
	 * @param parentmenuid
	 * @param isleaf
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.menu.update")
	@ResponseBody
	public WSResponse<Boolean> menu_update(
			@RequestParam(value = "menuid", required = true) Integer menuid,
			@RequestParam(value = "menuname", required = true) String menuname,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "parentmenuid", required = false) Integer parentmenuid,
			@RequestParam(value = "isleaf", required = false) Integer isleaf,
			HttpSession httpSession) throws Exception{
		if (parentmenuid!=null && parentmenuid>0) {
			isleaf = 1;
		}
		adminMenuRoleService.updateMenu(menuid, menuname, url, type, parentmenuid, isleaf);
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		response.add(true);
		if (menuid==null || menuid==0) {
			response.setRespDescription("新增菜单成功"); 
		}else{
			response.setRespDescription("更新菜单成功"); 			
		}
		return response;
	}
	
	/**
	 * 删除指定菜单
	 * @param menuid
	 * @param httpSession
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("admin.menu.remove")
	@ResponseBody
	public WSResponse<Boolean> menu_remove(
			@RequestParam(value = "menuid", required = true) Integer menuid,
			HttpSession httpSession) throws Exception{
		if (menuid==null || menuid==0) {
			throw new WException(ExceptionConst.INPUT_VALID_MENUID.intValue());
		}
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		adminMenuRoleService.deleteMenu(menuid);
		response.add(true);
		response.setRespDescription("删除菜单成功"); 	
		return response;
	}
	
	/**
	 * 角色菜单查询
	 * @param roleid
	 * @param httpSession
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("admin.rolemenu.query")
	@ResponseBody
	public WSResponse<RoleMenuDto> rolemenu_query(
			@RequestParam(value = "roleid", required = true) Integer roleid,
			HttpSession httpSession) throws Exception{
		if (roleid==null || roleid==0) {
			throw new WException(ExceptionConst.INPUT_VALID_ROLEID.intValue());
		}
		WSResponse<RoleMenuDto> response = new WSResponse<RoleMenuDto>();
		response.addAll(adminMenuRoleService.queryRoleMenu(roleid));
		response.setRespDescription("查询角色菜单成功");
		return response;
	}
	
	/**
	 * 更新角色的菜单
	 * @param roleid
	 * @param rolename
	 * @param roledesc
	 * @param menuidlist
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("admin.rolemenu.update")
	@ResponseBody
	public WSResponse<Boolean> rolemenu_update(
			@RequestParam(value = "roleid", required = true) Integer roleid,
			@RequestParam(value = "menuidlist", required = false) String menuidlist,
			HttpSession httpSession) throws Exception{
		if (roleid==null || roleid==0) {
			throw new WException(ExceptionConst.INPUT_VALID_ROLEID.intValue());
		}
		log.info("get menu:"+menuidlist);
		List<DBAdminRoleMenu> list = new ArrayList<DBAdminRoleMenu>();
		JSONArray arrs = JSONArray.fromObject(menuidlist);
		for (int i = 0; i < arrs.size(); i++) {
			DBAdminRoleMenu menu = IopUtils.jsonParseToObject(arrs.getJSONObject(i).toString(), DBAdminRoleMenu.class);
			menu.setRoleid(roleid);
			list.add(menu);
		}
		adminMenuRoleService.updateRoleMenu(roleid, list);
		shiroServeice.reCreateFilterChains();
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		response.add(true);
		response.setRespDescription("更新角色菜单成功");
		return response;
	}
	
	
}
