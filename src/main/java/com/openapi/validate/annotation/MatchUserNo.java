package com.openapi.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.openapi.validate.impl.MatchUserNoValidator;

@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Constraint(validatedBy = MatchUserNoValidator.class)
@Documented  
public @interface MatchUserNo {
	String message() default "请输入有效的用户号";  
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
