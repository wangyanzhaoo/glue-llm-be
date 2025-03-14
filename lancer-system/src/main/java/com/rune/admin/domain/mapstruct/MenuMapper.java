package com.rune.admin.domain.mapstruct;

import com.rune.admin.domain.entity.Menu;
import com.rune.admin.domain.vo.MenuView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author avalon
 * @date 22/7/14 15:06
 * @description 菜单 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends BaseMapper<Menu, MenuView> {
}
