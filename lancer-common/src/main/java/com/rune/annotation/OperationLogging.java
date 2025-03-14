package com.rune.annotation;

import com.rune.enums.LogTypeEnum;

import java.lang.annotation.*;

/**
 * @author avalon
 * @date 22/8/5 14:12
 * @description 操作日志注解
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLogging {

    /**
     * 日志信息
     *
     * @return 日志描述信息
     */
    String msg() default "";

    /**
     * 日志操作类型
     *
     * @return 日志操作类型枚举
     */
    LogTypeEnum type();

    /**
     * 是否保存方法入参
     *
     * @return boolean
     */
    boolean recordParams() default false;

    /**
     * 是否保存方法返回值
     *
     * @return boolean
     */
    boolean recordResult() default false;

}
