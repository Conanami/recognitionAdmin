package com.openapi.validate.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.common.util.IopUtils;
import com.openapi.validate.annotation.MatchUserNo;

public class MatchUserNoValidator implements ConstraintValidator<MatchUserNo, String> {

	@Override
	public void initialize(MatchUserNo constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (IopUtils.isNotEmpty(value)) {
			return true;
		}
		return false;
	}

}
