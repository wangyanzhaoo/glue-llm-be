package com.rune.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictKey {

    /**
     * 字典 key
     */
    String value() default "";

    /**
     * 是否展示整个对象
     */
    boolean insideObject() default false;

}
