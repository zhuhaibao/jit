package com.jumper.jit.aspect.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MultipartFileValidator.class)
public @interface ValidFile {
    boolean escapeFromHibernate() default true;

    String message() default "上传文件不合格";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowEmpty() default false;

    boolean ignoreCase() default true;

    String[] endWith() default {};

    int max() default 5 * 1024 * 1024;//5M
}
