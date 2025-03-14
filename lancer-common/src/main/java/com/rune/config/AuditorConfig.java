package com.rune.config;

import com.rune.utils.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author avalon
 * @date 22/7/20 17:04
 * @description 设置审计
 */
@Component("auditorAware")
public class AuditorConfig implements AuditorAware<String> {

    /**
     * 用于实体 BaseEntity 自动获取用户名
     *
     * @return 用户名
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUsername());
    }
}
