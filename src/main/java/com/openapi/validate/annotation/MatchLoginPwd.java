package com.openapi.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.openapi.validate.impl.MatchLoginPwdValidator;

@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Constraint(validatedBy = MatchLoginPwdValidator.class)
@Documented  
public @interface MatchLoginPwd {
	String message() default "请输入有效的登录密码";  
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
