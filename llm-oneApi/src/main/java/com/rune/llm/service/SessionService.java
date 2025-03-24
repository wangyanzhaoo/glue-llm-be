package com.rune.llm.service;

import com.rune.exception.BadRequestException;
import com.rune.llm.domain.dto.SessionQuery;
import com.rune.llm.domain.entity.QSession;
import com.rune.llm.domain.entity.Session;
import com.rune.llm.domain.mapstruct.SessionMapper;
import com.rune.llm.domain.vo.SessionView;
import com.rune.llm.repository.SessionRepo;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.SecurityUtils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 会话表接口实现
 */
@Service
@RequiredArgsConstructor
public class SessionService {

    final JPAQueryFactoryPrimary queryFactory;
    private final SessionRepo sessionRepo;
    private final SessionMapper sessionMapper;

    public Page<SessionView> queryAll(SessionQuery query) {
        Page<Session> page = sessionRepo.findAll(PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(sessionMapper::toVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Session resources) {
        resources.setUserId(SecurityUtils.getCurrentId());
        sessionRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Session resources) {
        resources.setUserId(null);
        Session session = sessionRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("更新的会话表不存在"));
        UpdateUtil.copyNullProperties(session, resources);
        sessionRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTitle(Long sessionId, String title) {
        QSession qSession = QSession.session;
        queryFactory.update(qSession).set(qSession.title, title)
                .where(qSession.id.eq(sessionId), qSession.userId.eq(SecurityUtils.getCurrentId())).execute();
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        QSession qSession = QSession.session;
        queryFactory.update(qSession).set(qSession.isDelete, true).where(qSession.id.in(ids)).execute();
    }
}
