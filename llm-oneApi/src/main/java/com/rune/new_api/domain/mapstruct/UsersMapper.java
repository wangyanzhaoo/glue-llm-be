package com.rune.new_api.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.new_api.domain.entity.Users;
import com.rune.new_api.domain.vo.UsersView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description Users Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsersMapper extends BaseMapper<Users, UsersView> {


}
