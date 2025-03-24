package com.rune.new_api.service;

import com.rune.exception.BadRequestException;
import com.rune.new_api.domain.dto.TokensQuery;
import com.rune.new_api.domain.entity.QTokens;
import com.rune.new_api.domain.entity.Tokens;
import com.rune.new_api.domain.mapstruct.TokensMapper;
import com.rune.new_api.domain.vo.TokensView;
import com.rune.new_api.repository.TokensRepo;
import com.rune.querydsl.JPAQueryFactorySecond;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api Api令牌表接口实现
 */
@Service
@RequiredArgsConstructor
public class TokensService {

    final JPAQueryFactorySecond queryFactory;
    private final TokensRepo tokensRepo;
    private final TokensMapper tokensMapper;
    ;

    public Page<TokensView> queryAll(TokensQuery query) {
        Page<Tokens> page = tokensRepo.findAll(PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(tokensMapper::toVo);
    }

    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)
    public void create(Tokens resources) {
        tokensRepo.save(resources);
    }

    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)
    public void update(Tokens resources) {
        Tokens tokens = tokensRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("更新的new-api Api令牌表不存在"));
        UpdateUtil.copyNullProperties(tokens, resources);
        tokensRepo.save(resources);
    }

    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        QTokens qTokens = QTokens.tokens;
        queryFactory.update(qTokens).set(qTokens.deletedAt, LocalDateTime.now()).where(qTokens.id.in(ids)).execute();
    }
}
