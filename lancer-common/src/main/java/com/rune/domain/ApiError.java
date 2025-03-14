package com.rune.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author avalon
 * @date 22/4/7 17:39
 * @description /
 */
@Schema(title = "Api 请求成功返回封装")
@Getter
@Setter
public class ApiError {

    private boolean success;

    private String errorMessage;

    private Integer showType;

    private LocalDateTime timestamp;

    public ApiError() {
        showType = 3;
        timestamp = LocalDateTime.now();
    }

    public ApiError(String errorMessage) {
        showType = 3;
        this.errorMessage = errorMessage;
        timestamp = LocalDateTime.now();
    }
}
