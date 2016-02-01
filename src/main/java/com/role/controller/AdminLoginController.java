package com.role.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mybatis.one.po.DBAdminUserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.exception.ExceptionFormatter;
import com.common.util.MD5;
import com.common.util.WSResponse;
import com.role.service.IAdminMenuRoleService;
import com.role.service.IAdminUserRoleService;
import com.role.service.IAdminUserService;

@Controller
public class AdminLoginController {
	private static Logger log = LoggerFactory.getLogger(AdminLoginController.class);
	 
	@Resource
	IAdminUserRoleService adminUserRoleService;
	
	@Resource
	IAdminUserService adminuserService;
	
	@Resource
	IAdminMenuRoleService adminRoleMenuService;
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public WSResponse<Boolean> exceptionHandler(Exception ex, HttpSession httpSession) {
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		log.error("Exception异常：" + ex);
		ExceptionFormatter.setResponse(response, ex);
		return response;
	}
	
	/**
	 * 后台管理登录页面
	 * @param username
	 * @param password
	 * @param servletResponse
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("sxd.admin.login")
	@ResponseBody
	public WSResponse<Boolean> register(
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "password", required = false) String password,
			HttpServletResponse servletResponse,
			HttpSession httpSession) throws Exception{
		WSResponse<Boolean> response = new WSResponse<Boolean>();
		String pswtoMD5 =  MD5.encrypt(password);//数据库中存放MD5加密后的大写密文，后期会修改从页面提交过来也会是密文
		
		// 使用 shiro 登录
		SecurityUtils.getSubject().login(new UsernamePasswordToken(username, pswtoMD5));
		//获取菜单
		Integer userid = (Integer) httpSession.getAttribute("userid");
		JSONArray  menuArr = getMenu(userid);
		httpSession.setAttribute("menuArray", menuArr);
		
		response.setRespDescription("登录成功");
		return response;
	}
	
	/**
	 * 退出,登出
	 * @return
	 */
	@RequestMapping(value="/login/out")
	public String loginOut(HttpSession httpSession){
		Enumeration<String> attrList =  httpSession.getAttributeNames();
		while(attrList.hasMoreElements()){
			String attrName = attrList.nextElement();
			httpSession.removeAttribute(attrName);
		}
		// shiro 权限退出
		SecurityUtils.getSubject().logout();
		return "/jsp/Login.jsp";
		
	}
	
	 /**
     * 根据userid获取菜单
     * @param userid
     * @return
     */
    public JSONArray getMenu(Integer userid){

		List<RoleMenuDto> viewMenuRoleResultList = new ArrayList<RoleMenuDto>();
		JSONArray menuArray  = new JSONArray();
		JSONArray firstLevel  =  new JSONArray();
		JSONArray secondLevel =  new JSONArray();
       //根据userid获取对应的所有的角色id
		List<DBAdminUserRole> adminUserRoleList = adminUserRoleService.queryByUserid(userid);
		if(adminUserRoleList!=null && adminUserRoleList.size()>0){//用户已经分配了角色
			for(int i=0;i<adminUserRoleList.size();i++){//循环拿到角色
				DBAdminUserRole adminUserRole =  adminUserRoleList.get(i);
				Integer roleid = adminUserRole.getRoleid();
				//根据角色拿到角色对应的menu
				List<RoleMenuDto> viewMenuRoleList = adminRoleMenuService.queryMenuByRoleid(roleid);
				viewMenuRoleResultList.addAll(viewMenuRoleList);
			}
		}
	    //首先拼凑一级菜单  暂定为总共两级菜单 
	    for(int j=0;j<viewMenuRoleResultList.size();j++){
	    	RoleMenuDto viewMenuRole =  viewMenuRoleResultList.get(j);
	    	if(viewMenuRole.getType().intValue()==1){//一级菜单
	    		JSONObject objFirst  = new JSONObject();
	    		objFirst.put("menuid",viewMenuRole.getMenuid());
	    		objFirst.put("text",viewMenuRole.getMenuname());
	    		objFirst.put("isexpand", true);
	    		firstLevel.add(objFirst);
	    	}else if(viewMenuRole.getType().intValue()==2){
	    		JSONObject objSecond  = new JSONObject();
	    		objSecond.put("url",viewMenuRole.getUrl());
	    		objSecond.put("text",viewMenuRole.getMenuname());
	    		objSecond.put("parentmenuid", viewMenuRole.getParentmenuid());
	    		secondLevel.add(objSecond);
	    	}
	    }
	    for(int k=0;k<firstLevel.size();k++ ){//拼凑
	    	JSONObject objfir  =  new JSONObject();
	    	JSONArray temp =  new JSONArray();
	    	objfir = firstLevel.getJSONObject(k);
	    	String firstmenuid =  objfir.getString("menuid");
	    	for(int l=0;l<secondLevel.size();l++){//循环二级菜单
	    		JSONObject objsec  = secondLevel.getJSONObject(l);
	    		String secondparmentmenuid =  objsec.getString("parentmenuid");
	    		if(secondparmentmenuid.equals(firstmenuid)){
	    			JSONObject obj  =  new JSONObject();
	    			JSONObject objurl  =  new JSONObject();
	    			obj.put("text", objsec.getString("text"));
	    			objurl.put("url", objsec.getString("url"));
	    			obj.put("attributes", objurl);
	    			temp.add(obj);	
	    		}
	    	}
	    	JSONObject resultFir = new JSONObject();
	    	resultFir.put("text", objfir.getString("text"));
	    	resultFir.put("isexpand", true);
	    	resultFir.put("children",temp);
	    	menuArray.add(resultFir);
	    }
      return menuArray;
    	
    }
	 
	public static void main(String[] args) {
		try {
//			readExcel(new FileInputStream("C:/Users/huweiping/Downloads/10商消贷产品/客户列表.xlsx"), DataCustomDto.class);
//			CSVParser parser = CSVParser.parse(csvData, Charset.forName("GBK"),
//					CSVFormat.TDF);
//			for (CSVRecord csvRecord : parser) {
//					log.info("num:"+csvRecord.size());
//			}
//			String encoding = "GBK";
//			InputStreamReader read = new InputStreamReader(new FileInputStream(
//					"C:\\tools\\custom.txt"), encoding);
//			BufferedReader bufferedReader = new BufferedReader(read);
//            String lineTxt = null;
//            while((lineTxt = bufferedReader.readLine()) != null){
//            	StringTokenizer st = new StringTokenizer(lineTxt, "\t");
//            	while (st.hasMoreTokens()) { 
//            		String commaSplit = st.nextToken();
//            		System.out.print("\t"+commaSplit);
//            	}
//            	System.out.println("");
//            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
