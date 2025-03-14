package com.rune.admin.service;

import com.querydsl.core.BooleanBuilder;
import com.rune.admin.domain.dto.DataRuleQuery;
import com.rune.admin.domain.entity.DataRule;
import com.rune.admin.domain.entity.QDataRule;
import com.rune.admin.domain.entity.User;
import com.rune.admin.domain.mapstruct.DataRuleMapper;
import com.rune.admin.domain.vo.DataRuleView;
import com.rune.admin.repository.DataRuleRepo;
import com.rune.admin.repository.UserRepo;
import com.rune.admin.util.DataRuleUtil;
import com.rune.exception.BadRequestException;
import com.rune.utils.RedisUtils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 大方的脑壳
 * @date 2023/4/20 16:19
 * @description 数据规则接口实现
 */
@Service
@RequiredArgsConstructor
public class DataRuleService {

    private final DataRuleRepo dataRuleRepo;

    private final UserRepo userRepo;

    private final DataRuleMapper dataRuleMapper;

    private final RedisUtils redisUtils;

    public Page<DataRuleView> queryAll(DataRuleQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        QDataRule qDataRule = QDataRule.dataRule;
        if (StringUtils.isNotBlank(query.getName())) builder.and(qDataRule.name.contains(query.getName()));
        Page<DataRule> page = dataRuleRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(dataRuleMapper::toVo);
    }

    public List<DataRuleView> select() {
        List<DataRule> all = dataRuleRepo.findAll();
        return dataRuleMapper.toVo(all);
    }

    public void create(DataRule resources) {
        dataRuleRepo.save(resources);
    }

    @Transactional
    public void update(DataRule resources) {
        DataRule dataRule = dataRuleRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("数据规则不存在"));
        UpdateUtil.copyNullProperties(dataRule, resources);
        dataRuleRepo.save(resources);
        Set<String> strings = redisUtils.keyScanner("info:").stream().map(key -> key.toString().split(":")[1]).collect(Collectors.toSet());
        // 获取redis中登陆用户的数据规则id
        List<User> byDataRuleId = userRepo.findByUsernameIn(strings);
        if (byDataRuleId != null) {
            List<Long> fetch = byDataRuleId.stream().map(User::getId).toList();
            DataRuleUtil.getAllowUsername(new HashSet<>(fetch));
        }
    }

    public void delete(Long id) {
        if (userRepo.countByDataRuleId(id) > 0)
            throw new BadRequestException("当前数据规则已被用户绑定");
        dataRuleRepo.deleteById(id);
    }
}
