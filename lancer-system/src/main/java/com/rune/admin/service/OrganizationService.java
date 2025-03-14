package com.rune.admin.service;

import com.rune.admin.domain.entity.Organization;
import com.rune.admin.domain.mapstruct.OrganizationMapper;
import com.rune.admin.domain.vo.OrganizationView;
import com.rune.admin.repository.OrganizationRepo;
import com.rune.admin.repository.UserRepo;
import com.rune.exception.BadRequestException;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 大方的脑壳
 * @date 2023/4/19 17:11
 * @description 组织架构接口实现
 */
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepo organizationRepo;

    private final OrganizationMapper organizationMapper;

    private final UserRepo userRepo;

    public List<OrganizationView> queryAll() {
        return organizationMapper.toVo(organizationRepo.findAll(Sort.by(Sort.Direction.ASC, "sort")));
    }

    public void create(Organization resources) {
        if (resources.getPid() != 0L) {
            Organization organization = organizationRepo.findById(resources.getPid()).orElseThrow(() -> new BadRequestException("组织架构不存在"));
            resources.setHierarchy(organization.getHierarchy() + "-" + resources.getPid());
            resources.setDepth(organization.getDepth() + 1);
        }
        organizationRepo.save(resources);
    }

    public void update(Organization resources) {
        Organization organization = organizationRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("组织架构不存在"));
        UpdateUtil.copyNullProperties(organization, resources);
        organizationRepo.save(resources);
    }

    public void delete(Long id) {
        if (organizationRepo.countByPid(id) > 0)
            throw new BadRequestException("组织架构存在子节点，请先删除所有子节点");
        if (userRepo.countByOrganizationId(id) > 0)
            throw new BadRequestException("组织架构已被用户使用，无法删除");
        organizationRepo.deleteById(id);
    }
}
