package com.openapi.validate.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.openapi.validate.annotation.MatchDateTime;

public class MatchDateTimeValidator implements ConstraintValidator<MatchDateTime, String>{

	private String formatString;
	@Override
	public void initialize(MatchDateTime arg0) {
		formatString = arg0.format();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext arg1) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		Calendar startday = Calendar.getInstance();
		try {
			startday.setTime(sdf.parse(value));
			return true;
		} catch (Exception e) {
			return false;
//			errors.rejectValue("startday", "请输入有效的开始日期，格式必须为yyyyMMdd");
		}
	}

}
