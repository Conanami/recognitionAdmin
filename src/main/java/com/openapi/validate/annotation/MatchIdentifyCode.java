package com.openapi.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.openapi.validate.impl.MatchIdentifyCodeValidator;

@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Constraint(validatedBy = MatchIdentifyCodeValidator.class)
@Documented  
public @interface MatchIdentifyCode {
	String message() default "请输入有效的身份证号码";  
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
