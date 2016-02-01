package com.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class WExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
																	Object handle, Exception ex) {
		WException wEx = null;
		if(ex instanceof WException){
			wEx = (WException) ex;
		} else {
			wEx = new WException("系统未知异常",ex);
		}
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", wEx.getMessage());
		modelAndView.setViewName(wEx.getUrl());
		
		return modelAndView;
	}

}
