package com.role.controller;

public class RolePermissionsDto {
	private Integer menuid;
	private String menuname;
	private String url;
	private String roles;
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
	 * @return the roles
	 */
	public String getRoles() {
		return roles;
	}
	/**
	 * @param roles the roles to set
	 */
	public void setRoles(String roles) {
		this.roles = roles;
	}
}
