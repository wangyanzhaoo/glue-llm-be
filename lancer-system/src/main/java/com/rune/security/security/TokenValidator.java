package com.rune.security.security;

import com.rune.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description Token 自定义验证
 */
@RequiredArgsConstructor
public class TokenValidator implements OAuth2TokenValidator<Jwt> {

    private final RedisUtils redisUtils;

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        // 当前时间
        Instant now = Instant.now();
        // token 到期时间
        Instant exp = token.getExpiresAt();
        // 计算时间差值
        Duration duration = Duration.between(now, exp);
        long minutes = duration.toMinutes();

        if (minutes > 30) {
            String redisToken = String.valueOf(redisUtils.get("token:" + token.getSubject()));
            // 判断 jwt 是否存在于缓存中，并且是否一致
            if (!Objects.equals(redisToken, "null") && Objects.equals(redisToken, token.getTokenValue()))
                return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, "授权认证无效", null));
    }
}
