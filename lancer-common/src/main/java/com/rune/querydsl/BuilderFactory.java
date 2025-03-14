package com.rune.querydsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.rune.domain.AuthInfo;
import com.rune.exception.BadRequestException;
import com.rune.utils.RedisUtils;
import com.rune.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author sedate
 * @date 2023/4/26 9:02
 * @description 基于数据规则的 Builder 工厂
 */
@RequiredArgsConstructor
@Component
public class BuilderFactory {

    private final RedisUtils redisUtils;

    private final ObjectMapper objectMapper;

    /**
     * @return 条件判断集
     */
    public BooleanBuilder getBuilder() {
        String key = "info:" + SecurityUtils.getCurrentUsername();
        AuthInfo authInfo = Objects.isNull(redisUtils.get(key)) ? null : objectMapper.convertValue(redisUtils.get(key), AuthInfo.class);
        if (authInfo == null) {
            throw new BadRequestException("数据规则不存在");
        }

        BooleanBuilder builder = new BooleanBuilder();
        Set<Long> allowOrganization = authInfo.getAllowOrganization();
        Long organizationId = authInfo.getOrganizationId();
        if (authInfo.getLookAll()) {
            return builder;
        }
        if (!allowOrganization.isEmpty()) {
            if (allowOrganization.contains(0)) {
                // 添加只能查看自己的规则
                builder.and(new MyStringPath("createBy").eq(SecurityUtils.getCurrentUsername()).and(new MyNumberPath(Long.class, "organizationId").eq(organizationId)));
            } else {
                builder.and(new MyNumberPath(Long.class, "organizationId").in(allowOrganization));
            }
        }
        return builder;
    }
}