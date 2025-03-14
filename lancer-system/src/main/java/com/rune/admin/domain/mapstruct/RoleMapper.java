package com.rune.admin.domain.mapstruct;

import com.rune.admin.domain.entity.Role;
import com.rune.admin.domain.vo.RoleSmallView;
import com.rune.admin.domain.vo.RoleView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @author avalon
 * @date 22/7/14 15:06
 * @description 角色 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends BaseMapper<Role, RoleView> {

    List<RoleSmallView> toSmallVo(List<Role> roleList);
}
