package com.rune.admin.service;

import com.querydsl.core.BooleanBuilder;
import com.rune.admin.domain.dto.RoleQuery;
import com.rune.admin.domain.entity.QRole;
import com.rune.admin.domain.entity.Role;
import com.rune.admin.domain.mapstruct.RoleMapper;
import com.rune.admin.domain.vo.RoleSmallView;
import com.rune.admin.domain.vo.RoleView;
import com.rune.admin.repository.RoleRepo;
import com.rune.exception.BadRequestException;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author avalon
 * @date 22/7/14 15:04
 * @description 角色接口实现
 */
@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepo roleRepo;

    private final RoleMapper roleMapper;

    public Page<RoleView> queryAll(RoleQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        QRole qRole = QRole.role;
        if (StringUtils.isNotBlank(query.getName())) builder.and(qRole.name.contains(query.getName()));
        Page<Role> page = roleRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(roleMapper::toVo);
    }

    public void create(Role resources) {
        roleRepo.findByName(resources.getName()).ifPresent(r -> {
            throw new BadRequestException("角色: " + r.getName() + " 已存在");
        });
        roleRepo.save(resources);
    }

    public void update(Role resources) {
        Role role = roleRepo.findById(resources.getId()).orElseThrow(
                () -> new BadRequestException("角色不存在")
        );
        UpdateUtil.copyNullProperties(role, resources);
        roleRepo.save(resources);
    }

    public void delete(Set<Long> ids) {
        Set<Long> roles = roleRepo.countByRoleId();
        if (!Collections.disjoint(ids, roles)) throw new BadRequestException("角色已被用户使用，无法删除");
        roleRepo.deleteAllByIdInBatch(ids);
    }

    public List<RoleSmallView> select() {
        List<Role> roleList = roleRepo.findAll();
        return roleMapper.toSmallVo(roleList);
    }
}
