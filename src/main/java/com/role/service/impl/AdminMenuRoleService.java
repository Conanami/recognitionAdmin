package com.role.service.impl;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import mybatis.one.mapper.DBAdminMenuMapper;
import mybatis.one.mapper.DBAdminRoleMenuMapper;
import mybatis.one.po.DBAdminMenu;
import mybatis.one.po.DBAdminMenuExample;
import mybatis.one.po.DBAdminRoleMenu;
import mybatis.one.po.DBAdminRoleMenuExample;

import org.springframework.stereotype.Service;

import com.common.exception.ExceptionConst;
import com.common.exception.WException;
import com.common.util.IopUtils;
import com.common.util.Page;
import com.common.util.WSResponse;
import com.role.controller.RoleMenuDto;
import com.role.controller.RolePermissionsDto;
import com.role.mybatis.mapper.CAdminRoleMapper;
import com.role.service.IAdminMenuRoleService;


@Service
public class AdminMenuRoleService implements IAdminMenuRoleService {
	
	@Resource
	DBAdminMenuMapper adminmenuMapper;
	
	@Resource
	DBAdminRoleMenuMapper adminRoleMenuMapper;
	
	@Resource
	CAdminRoleMapper cadminRoleMapper;
 
    /**
     * 根据角色id获取该角色下的菜单
     */
	
	public List<RoleMenuDto> queryMenuByRoleid(Integer roleid) {
		List<RoleMenuDto> list = cadminRoleMapper.selectAdminRoleMenu(roleid);
		//由于获取的是所有的菜单，这里需要过滤掉没有权限的菜单，然后返回到前台
		List<RoleMenuDto> result = new ArrayList<RoleMenuDto>();
		for (RoleMenuDto roleMenuDto : list) {
			if (roleMenuDto.getChecked()==1) {
				result.add(roleMenuDto);
			}
		}
		return result;
	}
	
	/**
	 * 查询所有的角色菜单以及权限
	 * @return
	 */
	public List<RolePermissionsDto> selectAllRoleMenuPermission(){
		return cadminRoleMapper.selectAllRoleMenuPermission();
	}
	/**
	 * 查询所有菜单
	 * @return
	 */
	public WSResponse<DBAdminMenu> queryAllMenu(Integer page ,Integer pagesize){
		DBAdminMenuExample ex = new DBAdminMenuExample();
		DBAdminMenuExample.Criteria ct = ex.createCriteria();
		ct.andMenuidIsNotNull();
		if (page!=null) {
			Page pg = new Page(0, pagesize);
			pg.setPageNo(page);
			ex.setPage(pg);			
		}
		ex.setOrderByClause(" parentmenuid asc ");
		WSResponse<DBAdminMenu> response = new WSResponse<DBAdminMenu>();
		
		List<DBAdminMenu> list = adminmenuMapper.selectByExample(ex);
		List<DBAdminMenu> result = new ArrayList<DBAdminMenu>();
		List<DBAdminMenu> tmp1 = new ArrayList<DBAdminMenu>();
		for (DBAdminMenu dto : list) {
			if (dto.getType()==1) {
				tmp1.add(dto);
			}
		}
		for (DBAdminMenu dto : tmp1) {
			result.add(dto);
			for (DBAdminMenu dto2 : list) {
				if (dto2.getParentmenuid()==dto.getMenuid()) {
					result.add(dto2);
				}
			}
		}
		response.setRows(result);
		return response;
	}
	
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
	public void updateMenu(Integer menuid, String menuname, String url, Integer type, Integer parentmenuid, Integer isleaf) throws Exception{
		if (IopUtils.isEmpty(menuname)) {
			throw new WException(ExceptionConst.INPUT_VALID_MenuName.intValue());
		}
		if (menuid==null || menuid==0) {
			//新增菜单
			DBAdminMenu menu = new DBAdminMenu();
			menu.setMenuname(menuname);
			menu.setUrl(url);
			menu.setType(type);
			menu.setParentmenuid(parentmenuid);
			menu.setIsleaf(isleaf);
			adminmenuMapper.insert(menu);
		}else{
			DBAdminMenu menu = adminmenuMapper.selectByPrimaryKey(menuid);
			menu.setMenuname(menuname);
			menu.setUrl(url);
			menu.setType(type);
			menu.setParentmenuid(parentmenuid);
			menu.setIsleaf(isleaf);
			adminmenuMapper.updateByPrimaryKey(menu);
		}
	}
	
	/**
	 * 删除指定菜单
	 * @param menuid
	 */
	public void deleteMenu(Integer menuid){
		//从菜单删除
		adminmenuMapper.deleteByPrimaryKey(menuid);
		//从角色菜单删除
		DBAdminRoleMenuExample ex = new DBAdminRoleMenuExample();
		ex.createCriteria().andMenuidEqualTo(menuid);
		adminRoleMenuMapper.deleteByExample(ex);
	}
	
	/**
	 * 查询角色的所属菜单
	 * @param roleid
	 * @return
	 */
	public List<RoleMenuDto> queryRoleMenu(Integer roleid){
		//将菜单按照一级目录，二级目录进行排序，展现更友好
		List<RoleMenuDto> list = cadminRoleMapper.selectAdminRoleMenu(roleid);
		List<RoleMenuDto> result = new ArrayList<RoleMenuDto>();
		List<RoleMenuDto> tmp1 = new ArrayList<RoleMenuDto>();
		for (RoleMenuDto dto : list) {
			if (dto.getType()==1) {
				tmp1.add(dto);
			}
		}
		for (RoleMenuDto dto : tmp1) {
			result.add(dto);
			for (RoleMenuDto dto2 : list) {
				if (dto2.getParentmenuid()==dto.getMenuid()) {
					result.add(dto2);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 更新指定角色的所属菜单
	 * @param roleid
	 * @param menuidlist
	 */
	public void updateRoleMenu(Integer roleid, List<DBAdminRoleMenu> menuidlist){
		{
			DBAdminRoleMenuExample ex = new DBAdminRoleMenuExample();
			ex.createCriteria().andRoleidEqualTo(roleid);
			adminRoleMenuMapper.deleteByExample(ex);			
		}
		for (int i = 0; i < menuidlist.size(); i++) {
			DBAdminRoleMenu role = menuidlist.get(i);
			adminRoleMenuMapper.insert(role);
		}
	}
}
