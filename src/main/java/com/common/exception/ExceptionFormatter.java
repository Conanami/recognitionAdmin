package com.common.exception;

import org.springframework.validation.BindException;

import com.common.util.WSResponse;

/**
 * 统一异常格式化处理器
 * @author huweiping
 *
 */
public class ExceptionFormatter {
	
	public static void setResponse(WSResponse<Boolean> response, Exception ex){
		if (ex instanceof WException) {
			Integer code = ((WException) ex).getCode();	
			response.setRespCode(code);
			response.setRespDescription(ex.getMessage());
		}else if(ex instanceof BindException){
			response.setRespCode(ExceptionConst.INPUT_VALID_COMMON.intValue());
			response.setRespDescription(((BindException) ex).getBindingResult().getAllErrors().get(0).getDefaultMessage());
		}else{
			// 未知异常
			ex.printStackTrace();
			response.setRespCode(ExceptionConst.INTERNALERROR.intValue());
			response.setRespDescription(ex.getMessage());
		}
	}
}
