package com.rune.admin.domain.mapstruct;

import com.rune.admin.domain.entity.Organization;
import com.rune.admin.domain.vo.OrganizationView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 大方的脑壳
 * @date 2023/4/19 17:11
 * @description 组织架构 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationMapper extends BaseMapper<Organization, OrganizationView> {


}
