package com.rune.admin.util;

import com.querydsl.core.BooleanBuilder;
import com.rune.admin.domain.entity.DataRule;
import com.rune.admin.domain.entity.Organization;
import com.rune.admin.domain.entity.QOrganization;
import com.rune.admin.domain.entity.User;
import com.rune.admin.repository.OrganizationRepo;
import com.rune.admin.repository.UserRepo;
import com.rune.domain.AuthInfo;
import com.rune.exception.BadRequestException;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sedate
 * @date 2023/4/21 10:27
 * @description
 */
@Component
public class DataRuleUtil {

    private static OrganizationRepo organizationRepo;

    private static UserRepo userRepo;

    private static RedisUtils redisUtils;

    private static JPAQueryFactoryPrimary queryFactory;

    @Autowired
    public DataRuleUtil(JPAQueryFactoryPrimary queryFactory, OrganizationRepo organizationRepo, UserRepo userRepo, RedisUtils redisUtils) {
        DataRuleUtil.organizationRepo = organizationRepo;
        DataRuleUtil.userRepo = userRepo;
        DataRuleUtil.redisUtils = redisUtils;
        DataRuleUtil.queryFactory = queryFactory;
    }

    public static void getAllowUsername(Set<Long> ids) {
        for (Long id : ids) {
            // 查询当前用户角色
            User user = userRepo.findById(id).orElseThrow(() -> new BadRequestException("暂无用户"));
            AuthInfo authInfoRedis = new AuthInfo();
            Organization organization = null;
            HashSet<Long> set = new HashSet<>();

            // 获取用户的组织
            if (user.getOrganizationId() != null) {
                organization = organizationRepo.findById(user.getOrganizationId()).orElseThrow(() -> new BadRequestException("组织未找到"));
                authInfoRedis.setOrganizationId(organization.getId());
            }

            // 获取用户的数据规则
            if (user.getDataRule() != null) {
                DataRule dataRule = user.getDataRule();
                String rule = dataRule.getRule().strip();
                QOrganization qOrganization = QOrganization.organization;
                switch (rule) {
                    case "alone" -> set.add(0L);// 如果是 alone 代表只能看自己，添加特殊数字0来进行标识
                    case "all" -> authInfoRedis.setLookAll(true);// all 可查看所有
                    case "=" -> set.add(organization.getId());// = 查看本部门
                    default -> {
                        // 获取所有可以查看的组织id
                        set = new HashSet<>(getOrganizationId(rule, organization, qOrganization));
                        // 移除掉管理员专属组织
                        set.remove(1L);
                    }
                }
                authInfoRedis.setRule(rule);
                authInfoRedis.setAllowOrganization(set);
            }
            redisUtils.set("info:" + user.getUsername(), authInfoRedis, 43200L);
        }
    }

    private static List<Long> getOrganizationId(String rule, Organization organization, QOrganization qOrganization) {
        Integer organizationDepth = organization.getDepth();
        String organizationHierarchy = organization.getHierarchy();
        // 设置是否可以查看其余部门标识
        BooleanBuilder builder = new BooleanBuilder();

        // 获取符合数据规则的组织id
        if (">=".equals(rule)) {
            // 查询当前 hierarchy 层级关系的子组织id
            builder.and(qOrganization.depth.gt(organizationDepth));
            builder.and(qOrganization.hierarchy.like(organizationHierarchy + "-" + organization.getId() + "%"));
            List<Long> fetch = queryFactory.select(qOrganization.id).from(qOrganization).where(builder).fetch();
            fetch.add(organization.getId());
            return fetch;
        } else if ("<=".equals(rule)) {
            // 根据 hierarchy 的层级关系查询上级
            String[] split = organizationHierarchy.split("-");
            ArrayList<Long> integers = new ArrayList<>();
            for (String s : split) {
                if (!"0".equals(s)) {
                    integers.add(Long.valueOf(s));
                }
            }
            integers.add(organization.getId());
            return integers;
        }
        // 依据上述判断产生的条件查询所有符合的组织id
        return queryFactory.select(qOrganization.id).from(qOrganization).where(builder).fetch();
    }

}

