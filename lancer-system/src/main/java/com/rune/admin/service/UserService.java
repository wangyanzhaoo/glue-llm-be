package com.rune.admin.service;

import com.querydsl.core.BooleanBuilder;
import com.rune.admin.domain.dto.PasswordRequest;
import com.rune.admin.domain.dto.UserQuery;
import com.rune.admin.domain.entity.*;
import com.rune.admin.domain.mapstruct.UserMapper;
import com.rune.admin.domain.vo.UserView;
import com.rune.admin.repository.UserRepo;
import com.rune.admin.util.DataRuleUtil;
import com.rune.exception.BadRequestException;
import com.rune.new_api.domain.entity.Users;
import com.rune.new_api.repository.UsersRepo;
import com.rune.new_api.service.UsersService;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.SecurityUtils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description 用户接口实现
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepo userRepo;

    final UsersRepo apiUsersRepo;

    final UsersService apiUsersService;

    private final UserMapper userMapper;

    private final JPAQueryFactoryPrimary queryFactory;

    private final PasswordEncoder passwordEncoder;


    public Page<UserView> queryAll(UserQuery query) throws Exception {
        BooleanBuilder builder = new BooleanBuilder();
        QUser qUser = QUser.user;
        // 不查询当前登录用户
//        builder.and(qUser.id.ne(SecurityUtils.getCurrentId()));
        if (query.getOrganizationId() != null) builder.and(qUser.organizationId.eq(query.getOrganizationId()));
        if (StringUtils.isNotBlank(query.getUsername())) builder.and(qUser.username.contains(query.getUsername()));
        if (StringUtils.isNotBlank(query.getNickName())) builder.and(qUser.nickName.contains(query.getNickName()));
        if (StringUtils.isNotBlank(query.getPhone())) builder.and(qUser.phone.contains(query.getPhone()));
        if (StringUtils.isNotBlank(query.getEmail())) builder.and(qUser.email.contains(query.getEmail()));
        if (Objects.nonNull(query.getEnabled())) builder.and(qUser.enabled.eq(query.getEnabled()));

        Page<User> page = userRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize()));

        return page.map(userMapper::toVo);
    }

    @Transactional(value = "unionTransactionManager")
    public String create(User resources) {
        userRepo.findByUsername(resources.getUsername()).ifPresent(u -> {
            throw new BadRequestException("用户: " + u.getUsername() + " 已存在");
        });
        // 生成随机密码
        String password = RandomStringUtils.randomAscii(6);
        resources.setPassword(passwordEncoder.encode(password));
        Long organizationId = resources.getOrganizationId();
        // TODO 新增 修改 校验组织、数据权限范围是否存在。 角色是否需要？页面所有用到下拉数据（字典等）都要验证？
        checkRule(resources.getDataRule().getId());
        checkOrganization(organizationId);
        if (StringUtils.isBlank(resources.getUsedLimit())) {
            resources.setUsedLimit("0");
        }
        if (StringUtils.isBlank(resources.getRemainLimit())) {
            resources.setRemainLimit("0");
        }
        userRepo.save(resources);
        Long aLong = fillNewApiUserEntity(resources);
        resources.setApiUserId(aLong);
        userRepo.save(resources);
        return password;
    }

    private Long fillNewApiUserEntity(User resources) {
        Users users = new Users();
        users.setGroup(resources.getGroup());
        users.setUsername(resources.getUsername());
        users.setPassword("$2a$10$tjvjvoL/kZnd/3wBWRxoXefMod95UzHMfJxDrnJJxtb6To9xxfIy.");
        users.setDisplayName(resources.getNickName());
        users.setRole(1L);
        users.setStatus(1L);
        users.setQuota(Long.valueOf(resources.getRemainLimit()));
        users.setUsedQuota(Long.valueOf(resources.getUsedLimit()));
        users.setRequestCount(resources.getRequestCount());
        apiUsersRepo.save(users);
        return users.getId();
    }

    private void updateNewApiUserEntity(User resources) {
        Users users = new Users();
        users.setGroup(resources.getGroup());
        users.setDisplayName(resources.getNickName());
        users.setQuota(Long.valueOf(resources.getRemainLimit()));
        apiUsersService.update(users);
    }

    @Transactional
    public void update(User resources) {
        User user = userRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("用户不存在"));
        resources.setUsername(null);
        resources.setPassword(null);
        boolean flag = false;
        DataRule dataRule = resources.getDataRule();
        DataRule userDataRule = user.getDataRule();
        if (!Objects.equals(resources.getOrganizationId(), user.getOrganizationId())) {
            flag = true;
            if (!Objects.equals(resources.getOrganizationId(), user.getOrganizationId()))
                checkOrganization(resources.getOrganizationId());
        }
        if (Objects.nonNull(dataRule) && Objects.isNull(userDataRule)) {
            flag = true;
            checkRule(dataRule.getId());
        }
        if (Objects.nonNull(dataRule) && Objects.nonNull(userDataRule) && !Objects.equals(dataRule.getId(), userDataRule.getId())) {
            flag = true;
            checkRule(dataRule.getId());
        }
        UpdateUtil.copyNullProperties(user, resources);
        userRepo.save(resources);
        // 是否需要更新当前组织关联的用户
        if (flag) DataRuleUtil.getAllowUsername(Set.of(user.getId()));
        updateNewApiUserEntity(resources);
    }

    /**
     * 校验规则是否存在
     *
     * @param dataRuleId /
     */
    public void checkRule(Long dataRuleId) {
        QDataRule qDataRule = QDataRule.dataRule;
        if (Objects.nonNull(dataRuleId))
            Optional.ofNullable(queryFactory.selectFrom(qDataRule).where(qDataRule.id.eq(dataRuleId)).fetchFirst())
                    .orElseThrow(() -> new BadRequestException("选择的数据规则不存在"));
    }

    /**
     * 校验组织是否存在
     *
     * @param organizationId /
     */
    public void checkOrganization(Long organizationId) {
        QOrganization qOrganization = QOrganization.organization;
        if (Objects.nonNull(organizationId))
            Optional.ofNullable(queryFactory.selectFrom(qOrganization).where(qOrganization.id.eq(organizationId)).fetchFirst())
                    .orElseThrow(() -> new BadRequestException("选择的组织不存在"));
    }

    public void delete(Set<Long> ids) {
        Long currentId = SecurityUtils.getCurrentId();
        // 无法删除登录用户
        if (ids.contains(currentId)) throw new BadRequestException("无法删除当前登录的用户");
        userRepo.deleteAllByIdInBatch(ids);
        apiUsersService.delete(ids);
    }

    public Object userInfo(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new BadRequestException(HttpStatus.UNAUTHORIZED, "用户不存在"));

        record Auth(UserView user, Set<String> auth) {
        }
        return new Auth(userMapper.toVo(user), SecurityUtils.getCurrentAuth());
    }

    public void updatePassword(PasswordRequest resources) {
        User user = userRepo.findById(SecurityUtils.getCurrentId()).orElseThrow(() -> new BadRequestException("用户不存在"));
        if (!passwordEncoder.matches(resources.getOldPassWord(), user.getPassword())) {
            throw new BadRequestException("旧密码错误");
        } else if (!resources.getNewPassWord().equals(resources.getConfirmPassWord())) {
            throw new BadRequestException("新密码和确认密码输入不一致");
        } else if (resources.getConfirmPassWord().equals(resources.getOldPassWord())) {
            throw new BadRequestException("新密码和旧密码输入相同");
        }
        user.setPassword(passwordEncoder.encode(resources.getConfirmPassWord()));
        userRepo.save(user);
    }

    public String resetPassword(Long id) {
        if (id.equals(SecurityUtils.getCurrentId())) throw new BadRequestException("不可重置当前登录用户");
        User user = userRepo.findById(id).orElseThrow(() -> new BadRequestException("用户不存在"));
        String password = RandomStringUtils.randomAscii(6);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        return password;
    }
}
