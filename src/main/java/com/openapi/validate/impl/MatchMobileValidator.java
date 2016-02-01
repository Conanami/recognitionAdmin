package com.openapi.validate.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.common.util.IopUtils;
import com.openapi.validate.annotation.MatchMobile;

public class MatchMobileValidator implements ConstraintValidator<MatchMobile, String>{

	@Override
	public void initialize(MatchMobile constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value!=null && value.length()>0 && IopUtils.isMobile(value)) {
			return true;
		}
		return false;
	}

}
