package com.role.shiro;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.common.util.IopUtils;
import com.role.controller.RolePermissionsDto;
import com.role.service.IAdminMenuRoleService;

@Service
public class ShiroAuthService {
	private static Logger log = LoggerFactory.getLogger(ShiroAuthService.class);
	
    /**
     * 默认premission字符串
     */
    public static final String PREMISSION_STRING="roleOrFilter[\"{0}\"]";
    private String filterChainDefinitions = "";
    
    @Resource
	IAdminMenuRoleService adminRoleMenuService;
    
    @Resource
    private ShiroFilterFactoryBean shiroFilterFactoryBean;
    
	public String loadFilterChainDefinitions() {
		String fiter = "";// 改正后的url配置
		List<RolePermissionsDto> list = adminRoleMenuService
				.selectAllRoleMenuPermission();
		for (Iterator<RolePermissionsDto> it = list.iterator(); it.hasNext();) {
			RolePermissionsDto privilege = it.next();
			// 追加beans.xml中已经有的过滤
			if (!IopUtils.isEmpty(privilege.getUrl())) {
				fiter += "/"
						+ privilege.getUrl()
						+ " = authc,"
						+ MessageFormat.format(PREMISSION_STRING,
								privilege.getRoles()) + "\n";
			}
		}
		return filterChainDefinitions + fiter;
	}
    
	/**
	 * 重新生成最新的 角色和菜单对应关系
	 */
	public synchronized void reCreateFilterChains() {

		AbstractShiroFilter shiroFilter = null;
		try {
			shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean
					.getObject();
		} catch (Exception e) {
			log.error("getShiroFilter from shiroFilterFactoryBean error!", e);
			throw new RuntimeException(
					"get ShiroFilter from shiroFilterFactoryBean error!");
		}

		PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
				.getFilterChainResolver();
		DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
				.getFilterChainManager();

		// 清空老的权限控制, 这里如果清空掉所有的权限控制，会导致默认定义在shiro.xml文件里面的权限控制丢失掉，所以不能清空。
		//  而且由于superadmin肯定拥有所有权限，所以不会出现网上说的旧的权限无法被覆盖
		// manager.getFilterChains().clear();
		// shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
		shiroFilterFactoryBean
				.setFilterChainDefinitions(loadFilterChainDefinitions());
		// 重新构建生成
		Map<String, String> chains = shiroFilterFactoryBean
				.getFilterChainDefinitionMap();
		for (Map.Entry<String, String> entry : chains.entrySet()) {
			String url = entry.getKey();
			String chainDefinition = entry.getValue().trim().replace(" ", "");
			manager.createChain(url, chainDefinition);
		}
		log.info("重新加载角色菜单权限filterChainDefinitionMap完成");
	}
}
