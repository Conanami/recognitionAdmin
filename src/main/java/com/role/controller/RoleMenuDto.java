package com.role.controller;

import java.io.Serializable;

public class RoleMenuDto implements Serializable {

	private Integer roleid;
	private String rolename;
	private Integer menuid;
	private String menuname;
	private String icon;
	private String url;
	private Integer type;
	private Integer parentmenuid;
	private String parentmenuname;
	private Integer isleaf;
	private Integer position;
	private Integer checked;
	private static final long serialVersionUID = 1L;
	
	/**
	 * @return the roleid
	 */
	public Integer getRoleid() {
		return roleid;
	}
	/**
	 * @param roleid the roleid to set
	 */
	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}
	/**
	 * @return the menuid
	 */
	public Integer getMenuid() {
		return menuid;
	}
	/**
	 * @param menuid the menuid to set
	 */
	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}
	/**
	 * @return the menuname
	 */
	public String getMenuname() {
		return menuname;
	}
	/**
	 * @param menuname the menuname to set
	 */
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * @return the parentmenuid
	 */
	public Integer getParentmenuid() {
		return parentmenuid;
	}
	/**
	 * @param parentmenuid the parentmenuid to set
	 */
	public void setParentmenuid(Integer parentmenuid) {
		this.parentmenuid = parentmenuid;
	}
	/**
	 * @return the isleaf
	 */
	public Integer getIsleaf() {
		return isleaf;
	}
	/**
	 * @param isleaf the isleaf to set
	 */
	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}
	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}
	public String getParentmenuname() {
		return parentmenuname;
	}
	public void setParentmenuname(String parentmenuname) {
		this.parentmenuname = parentmenuname;
	}
	public Integer getChecked() {
		return checked;
	}
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
}