package com.common.exception;


public class WException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;
	
	private String url;
	
	private Integer code = 0;

	@SuppressWarnings("unused")
	private WException(String message) {
		super(message);
		this.message = message;
	}
	
	@SuppressWarnings("unused")
	private WException(String message, Integer code) {
		super(message);
		this.message = message;
		this.code = code;
	}
	
	public WException(Integer code){
		this.code = code;
		this.message = ExceptionConst.fromInt(code).getMsg();
	}
	
	public WException(String message, Throwable t) {
		super(message,t);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public WException setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
