package com.rune.config.service;

import com.querydsl.core.BooleanBuilder;
import com.rune.config.domain.dto.ConfigQuery;
import com.rune.config.domain.entity.Config;
import com.rune.config.domain.entity.QConfig;
import com.rune.config.domain.mapstruct.ConfigMapper;
import com.rune.config.domain.vo.ConfigView;
import com.rune.config.repository.ConfigRepo;
import com.rune.exception.BadRequestException;
import com.rune.utils.Configiutils;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author wangyz
 * @date 2023/11/30 09:20
 * @description 配置项接口实现
 */
@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "config")
public class ConfigService implements Configiutils {

    private final ConfigRepo configRepo;

    private final ConfigMapper configMapper;

    public Page<ConfigView> queryAll(ConfigQuery query) {
        BooleanBuilder builder = new BooleanBuilder();
        QConfig qConfig = QConfig.config;
        if (StringUtils.isNotBlank(query.getName())) builder.and(qConfig.name.contains(query.getName()));
        if (StringUtils.isNotBlank(query.getCode())) builder.and(qConfig.code.contains(query.getCode()));
        if (StringUtils.isNotBlank(query.getValue())) builder.and(qConfig.value.contains(query.getValue()));
        Page<Config> page = configRepo.findAll(builder, PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(configMapper::toVo);
    }

    @Override
    @Cacheable(key = "'all'")
    public Map<String, String> data() {
        return configRepo.findAll().stream()
                .peek(config -> {
                    if (config.getCode().equals("passwordComplexity")) {
                        config.setValue(findPasswordVerify(config.getValue()));
                    }
                }).collect(Collectors.toMap(
                        Config::getCode,
                        Config::getValue
                ));
    }

    @CacheEvict(allEntries = true)
    public void create(Config resources) {
        configRepo.findByCode(resources.getCode()).ifPresent((ne) -> {
            throw new BadRequestException("配置项已存在");
        });
        configRepo.save(resources);
    }

    @CacheEvict(allEntries = true)
    public void update(Config resources) {
        Config config = configRepo.findByCode(resources.getCode()).orElseThrow(() -> new BadRequestException("配置项不存在"));
        UpdateUtil.copyNullProperties(config, resources);
        configRepo.save(resources);
    }

    @CacheEvict(allEntries = true)
    public void delete(Long id) {
        Config config = configRepo.findById(id).orElseThrow(() -> new BadRequestException("配置项不存在"));
        config.setIsDelete(true);
        configRepo.save(config);
    }

    /**
     * 存储密码复杂度校验正则表达式
     *
     * @param resources /
     */
    public String findPasswordVerify(String resources) {
        return switch (resources) {
            case "0" -> "^.*$";
            default -> """
                    ^%s.{6,15}$""".formatted(resources.codePoints()
                    .mapToObj(c -> switch (c) {
                        case '1' -> "(?=.*[a-z])";
                        case '2' -> "(?=.*[A-Z])";
                        case '3' -> "(?=.*\\d)";
                        case '4' -> "(?=.*[!@#$%^&*()=_+;':,.?])";
                        default -> "";
                    })
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining()));
        };
    }
}
