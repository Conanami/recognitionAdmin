package com.common.exception;

import java.util.HashMap;
import java.util.Map;

public enum ExceptionConst {
	TIME_OUT(40001, "请求超时")
	,HTTPRequestError(40002, "请求错误")
	,UNKNOWNERROR(50000, "未知异常")
	,INTERNALERROR(40002, "系统内部错误")
	,SIGN_VALID_FAIL(40003, "请求参数签名错误")
	
	,INPUT_VALID_COMMON(40010, "输入异常")
	,INPUT_VALID_BANKACCOUNT(40004, "请输入有效的银行卡号")
	,INPUT_VALID_BANKCODE(40005, "请输入有效的银行编码")
	,INPUT_VALID_IDNAME(40006, "请输入有效的用户姓名")
	,INPUT_VALID_IDNO(40007, "请输入有效的证件号码")
	,INPUT_VALID_IDENTTYPE(40008, "请输入有效的证件类别")
	 
	,INPUT_VALID_MENUID(50001, "请输入有效的菜单编号")
	,INPUT_VALID_ROLEID(50002, "请输入有效的角色编号")
	,INPUT_VALID_UserID(50003, "请输入有效的用户名")
	,INPUT_VALID_MenuName(50004, "请输入有效的菜单名称")
	,INPUT_VALID_UserID_Exist(50005, "用户名已经存在")
	

	,SettingFileNotFound(60001, "配置文件不存在")
	,IoError(60002, "IO异常")
	
	,MerchIdNotFound(70003, "商户信息不存在")
	
	;
	
	
	private Integer errid;
	private String errmsg;
	private ExceptionConst(Integer errid, String msg)
	{
		this.errid = errid;
		this.errmsg = msg;
	}
	public Integer intValue(){
		return this.errid;
	}
	public String getMsg(){
		return this.errmsg;
	}
    private static final Map<Integer, ExceptionConst> stringToEnum = new HashMap<Integer, ExceptionConst>();
    static {
        for(ExceptionConst code : values()) {
            stringToEnum.put(code.errid, code);
        }
    }
    public static ExceptionConst fromInt(Integer errid) {
    	if (stringToEnum.containsKey(errid)) {
    		return stringToEnum.get(errid);						
		}else{
			return ExceptionConst.fromInt(UNKNOWNERROR.intValue());
		}
    }
}
