package com.openapi.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.openapi.validate.impl.MatchDateTimeValidator;

@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Constraint(validatedBy = MatchDateTimeValidator.class)
@Documented  
public @interface MatchDateTime {
	/***
	 * 日期格式化字符串
	 * @return
	 */
	String format() default "yyyyMMdd";
	String message() default "";  
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
