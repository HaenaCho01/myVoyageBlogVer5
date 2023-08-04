package com.sparta.myvoyageblog.exception.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS) // Source로 하면 사라지고, runtime까지는 유지할 필요 없음
public @interface MediaCheckUserNotEquals {
}