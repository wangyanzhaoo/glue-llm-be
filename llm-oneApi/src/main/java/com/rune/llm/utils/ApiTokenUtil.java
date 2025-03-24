package com.rune.llm.utils;

import com.rune.exception.BadRequestException;
import com.rune.llm.domain.entity.QApiToken;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.RedisUtils;
import com.rune.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yangkf
 * @date 2025/3/21 17:12
 * @description token 工具
 */
@Component
public class ApiTokenUtil {

    final static String API_TOKEN_PREFIX = "api_token:";

    static RedisUtils redisUtils;

    static JPAQueryFactoryPrimary queryFactoryPrimary;

    public static String getApiTokenKey() {
        String apiTokenKey = (String) redisUtils.get(API_TOKEN_PREFIX + SecurityUtils.getCurrentId());
        if (apiTokenKey != null) {
            return apiTokenKey;
        }
        QApiToken qApiToken = QApiToken.apiToken;
        String tokenKey = queryFactoryPrimary.select(qApiToken.key).from(qApiToken)
                .where(qApiToken.userId.eq(SecurityUtils.getCurrentId())).fetchOne();
        if (tokenKey == null) {
            throw new BadRequestException("当前用户还未创建api令牌");
        }
        redisUtils.set(API_TOKEN_PREFIX + SecurityUtils.getCurrentId(), tokenKey);
        return tokenKey;
    }

    @Autowired
    public void setStatic(RedisUtils redisUtils, JPAQueryFactoryPrimary queryFactoryPrimary) {
        ApiTokenUtil.redisUtils = redisUtils;
        ApiTokenUtil.queryFactoryPrimary = queryFactoryPrimary;
    }

}
