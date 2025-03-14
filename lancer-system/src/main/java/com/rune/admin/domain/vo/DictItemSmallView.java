package com.rune.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author avalon
 * @date 22/8/11 11:08
 * @description /
 */
@Schema(title = "字典项精简 页面VO")
@Getter
@Setter
public class DictItemSmallView implements Serializable {

    private String text;

    private String value;

    private List<DictItemSmallView> children;
}
