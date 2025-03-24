package com.rune.new_api.service;

import com.rune.exception.BadRequestException;
import com.rune.new_api.domain.dto.UsersQuery;
import com.rune.new_api.domain.entity.QUsers;
import com.rune.new_api.domain.entity.Users;
import com.rune.new_api.domain.mapstruct.UsersMapper;
import com.rune.new_api.domain.vo.UsersView;
import com.rune.new_api.repository.UsersRepo;
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
 * @description new-api 用户表接口实现
 */
@Service
@RequiredArgsConstructor
public class UsersService {

    final JPAQueryFactorySecond queryFactory;
    private final UsersRepo usersRepo;
    private final UsersMapper usersMapper;

    public Page<UsersView> queryAll(UsersQuery query) {
        Page<Users> page = usersRepo.findAll(PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(usersMapper::toVo);
    }

    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)
    public void create(Users resources) {
        usersRepo.save(resources);
    }

    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)
    public void update(Users resources) {
        Users users = usersRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("更新的new-api 用户表不存在"));
        UpdateUtil.copyNullProperties(users, resources);
        usersRepo.save(resources);
    }

    @Transactional(value = "secondaryTransactionManager", rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        QUsers qUsers = QUsers.users;
        queryFactory.update(qUsers).set(qUsers.deletedAt, LocalDateTime.now()).where(qUsers.id.in(ids)).execute();
    }
}
