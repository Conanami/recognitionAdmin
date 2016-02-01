package com.openapi.validate.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.common.util.IopUtils;
import com.openapi.validate.annotation.MatchIdentifyCode;

public class MatchIdentifyCodeValidator implements ConstraintValidator<MatchIdentifyCode, String> {
	
	@Override
	public void initialize(MatchIdentifyCode constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value!=null && value.length()>0 && IopUtils.isIdentifyCode(value)) {
			return true;
		}
		return false;
	}

}
