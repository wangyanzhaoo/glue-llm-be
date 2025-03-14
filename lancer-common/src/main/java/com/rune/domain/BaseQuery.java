package com.rune.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author avalon
 * @date 22/4/24 16:53
 * @description  /
 */
@Schema(title = "基本分页实体")
public class BaseQuery implements Serializable {

    private Integer current;

    private Integer pageSize;

    public BaseQuery() {
        current = 1;
        pageSize = 20;
    }

    public Integer getCurrent() {
        return current - 1;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
