package com.rune.llm.service;

import com.rune.exception.BadRequestException;
import com.rune.llm.domain.dto.ApiTokenQuery;
import com.rune.llm.domain.entity.ApiToken;
import com.rune.llm.domain.entity.QApiToken;
import com.rune.llm.domain.mapstruct.ApiTokenMapper;
import com.rune.llm.domain.vo.ApiTokenView;
import com.rune.llm.repository.ApiTokenRepo;
import com.rune.new_api.domain.entity.Tokens;
import com.rune.new_api.repository.TokensRepo;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.SecurityUtils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description api令牌表接口实现
 */
@Service
@RequiredArgsConstructor
public class ApiTokenService {

    final TokensRepo tokensRepo;
    final JPAQueryFactoryPrimary queryFactory;
    private final ApiTokenRepo apiTokenRepo;
    private final ApiTokenMapper apiTokenMapper;

    public Page<ApiTokenView> queryAll(ApiTokenQuery query) {
        Page<ApiToken> page = apiTokenRepo.findAll(PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(apiTokenMapper::toVo);
    }

    @Transactional(value = "unionTransactionManager", rollbackFor = Exception.class)
    public void create(ApiToken resources) {
        QApiToken qApiToken = QApiToken.apiToken;
        ApiToken apiToken = queryFactory.selectFrom(qApiToken)
                .where(qApiToken.userId.eq(SecurityUtils.getCurrentId())).fetchOne();
        if (apiToken != null) {
            throw new BadRequestException("当前用户已经存在令牌");
        }
        resources.setUserId(SecurityUtils.getCurrentId());
        resources.setKey(UUID.randomUUID().toString());
        apiTokenRepo.save(resources);
        fillNewApiTokensEntity(resources);
    }

    private void fillNewApiTokensEntity(ApiToken resources) {
        Tokens tokens = new Tokens();
        tokens.setKey(resources.getKey());
        tokens.setUserId(resources.getUserId());
        tokens.setGroup(resources.getGroup());
        tokens.setName(resources.getName());
        tokens.setStatus(resources.getStatus());
        tokens.setCreatedTime(new Date().getTime());
        tokens.setExpiredTime(resources.getExpireDate());
        tokens.setUnlimitedQuota(resources.getUnlimit() ? 1 : 0);
        tokens.setModelLimitsEnabled(resources.getModelLimit() ? 1 : 0);
        tokens.setModelLimits(resources.getModelLimitValue());
        tokens.setAllowIps(resources.getAllowIps());
        tokensRepo.save(tokens);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(ApiToken resources) {
        ApiToken apiToken = apiTokenRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("更新的api令牌表不存在"));
        UpdateUtil.copyNullProperties(apiToken, resources);
        apiTokenRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        QApiToken qApiToken = QApiToken.apiToken;
        queryFactory.update(qApiToken).set(qApiToken.isDelete, true).where(qApiToken.id.in(ids)).execute();
    }
}
