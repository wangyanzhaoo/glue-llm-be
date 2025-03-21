package com.rune.llm.service;

import com.rune.exception.BadRequestException;
import com.rune.llm.domain.dto.ChatQuery;
import com.rune.llm.domain.entity.Chat;
import com.rune.llm.domain.entity.QChat;
import com.rune.llm.domain.mapstruct.ChatMapper;
import com.rune.llm.domain.vo.ChatView;
import com.rune.llm.repository.ChatRepo;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.SecurityUtils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yangkf
 * @date 2025-03-21
 * @description 对话记录表接口实现
 */
@Service
@RequiredArgsConstructor
public class ChatService {

    final JPAQueryFactoryPrimary queryFactory;
    private final ChatRepo chatRepo;
    private final ChatMapper chatMapper;

    public Page<ChatView> queryAll(ChatQuery query) {
        Page<Chat> page = chatRepo.findAll(PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(chatMapper::toVo);
    }

    public List<ChatView> queryBySessionId(Long sessionId) {
        QChat qChat = QChat.chat;
        List<Chat> fetch = queryFactory.selectFrom(qChat).where(qChat.sessionId.eq(sessionId)).fetch();
        return fetch.stream().map(chatMapper::toVo).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Chat resources) {
        resources.setUserId(SecurityUtils.getCurrentId());
        chatRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Chat resources) {
        resources.setUserId(null);
        resources.setSessionId(null);
        Chat chat = chatRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("更新的对话记录表不存在"));
        UpdateUtil.copyNullProperties(chat, resources);
        chatRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        QChat qChat = QChat.chat;
        queryFactory.update(qChat).set(qChat.isDelete, true).where(qChat.id.in(ids)).execute();
    }
}
