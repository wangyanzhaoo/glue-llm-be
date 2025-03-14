package com.rune.querydsl;

import com.querydsl.core.types.dsl.NumberPath;

/**
 * @author sedate
 * @date 2023/5/4 13:57
 * @description 重写 queryDsl MyNumberPath 类中构造函数
 */
public class MyNumberPath extends NumberPath<Long> {

    public MyNumberPath(Class<Long> type, String var) {
        super(type, var);
    }
}
