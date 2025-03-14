package com.rune.admin.domain.mapstruct;

import com.rune.admin.domain.entity.User;
import com.rune.admin.domain.vo.UserView;
import com.rune.domain.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description 用户 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserView> {
}
