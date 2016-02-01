package com.openapi.validate.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.openapi.validate.annotation.MatchLoginPwd;

public class MatchLoginPwdValidator implements ConstraintValidator<MatchLoginPwd, String>{

	@Override
	public void initialize(MatchLoginPwd constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value!=null && value.length()>=6 && value.length()<=18) {
			return true;
		}
		return false;
	}

}
