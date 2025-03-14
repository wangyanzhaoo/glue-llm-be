package com.rune.querydsl;

import com.querydsl.core.types.dsl.StringPath;

/**
 * @author sedate
 * @date 2023/5/4 13:57
 * @description 重写 queryDsl StringPath 类中构造函数
 */
public class MyStringPath extends StringPath {

    public MyStringPath(String var) {
        super(var);
    }
}
