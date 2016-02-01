package com.role.shiro;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import com.common.util.IopUtils;
import com.role.controller.AdminMenuRoleController;
import com.role.controller.RolePermissionsDto;
import com.role.service.IAdminMenuRoleService;
/**
 * 该类只会在app启动的时候执行一次
 * @author huweiping
 *
 */
public class ChainDefinitionSectionMetaSource implements FactoryBean<Ini.Section>{
	private static Logger log = LoggerFactory.getLogger(AdminMenuRoleController.class);
	
    @Resource
	IAdminMenuRoleService adminRoleMenuService;

    private String filterChainDefinitions;

    /**
     * 默认premission字符串
     */
    public static final String PREMISSION_STRING="roleOrFilter[\"{0}\"]";

    @Override
	public Section getObject() throws Exception {

    	log.info("ChainDefinitionSectionMetaSource getObject");
    	 Ini ini = new Ini();//网上好多都是在这里配置URL的。但是发现是错误的。
         ini.load(filterChainDefinitions);
         Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
         return section;
    }

    public String getFilterChainDefinitions() {
		return filterChainDefinitions;
	}
    /**
     * 通过filterChainDefinitions对默认的url过滤定义
     * @param filterChainDefinitions 默认的url过滤定义
     */
    @Resource
	public void setFilterChainDefinitions(String filterChainDefinitions) {
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
		System.out.println(filterChainDefinitions + fiter);
		this.filterChainDefinitions = filterChainDefinitions + fiter;
	}

    public Class<?> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return false;
    }
}

