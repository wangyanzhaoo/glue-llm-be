package com.rune.security.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.rune.security.security.TokenValidator;
import com.rune.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description Oauth2 配置
 */
@RequiredArgsConstructor
@Configuration
public class Oauth2Config {

    private final RedisUtils redisUtils;
    @Value("${jwt.public.key}")
    private RSAPublicKey rsaPublicKey;
    @Value("${jwt.private.key}")
    private RSAPrivateKey rsaPrivateKey;

    private OAuth2TokenValidator<Jwt> tokenValidator() {
        return new TokenValidator(redisUtils);
    }

    // jwt加密器
    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(this.rsaPublicKey).privateKey(this.rsaPrivateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    // jwt解码器
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(this.rsaPublicKey).build();
        // 自定义jwt验证
        jwtDecoder.setJwtValidator(tokenValidator());
        return jwtDecoder;
    }

    // 设置提取权限
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("auth");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    /*
     从request请求中获取到token （用于自定义，现在无用
        @Bean
        public BearerTokenResolver bearerTokenResolver() {
            DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
            // 设置请求头的参数，即从这个请求头中获取到token
            bearerTokenResolver.setBearerTokenHeaderName(HttpHeaders.AUTHORIZATION);
            bearerTokenResolver.setAllowFormEncodedBodyParameter(false);
            // 是否可以从uri请求参数中获取token
            bearerTokenResolver.setAllowUriQueryParameter(false);
            return bearerTokenResolver;
        }
    */
}
