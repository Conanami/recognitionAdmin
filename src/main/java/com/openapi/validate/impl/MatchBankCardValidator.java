package com.openapi.validate.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.openapi.validate.annotation.MatchBankCard;

public class MatchBankCardValidator implements ConstraintValidator<MatchBankCard, String>{

	@Override
	public void initialize(MatchBankCard constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		String bankno = value;
		if (bankno!=null && bankno.matches("[0-9]+")
				&& bankno.length() >= 16
				&& bankno.length() <= 19) {
			return true;
		}
		return false;
	}

}
