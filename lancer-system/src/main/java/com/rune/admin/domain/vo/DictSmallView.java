package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;


/**
 * @author avalon
 * @date 22/8/11 11:08
 * @description /
 */
@Schema(title = "字典精简 页面VO")
@Getter
@Setter
public class DictSmallView implements Serializable {

    private String code;

    private Set<DictItemSmallView> items;
}

