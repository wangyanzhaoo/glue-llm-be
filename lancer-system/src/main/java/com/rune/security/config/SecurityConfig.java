package com.rune.security.config;

import com.rune.exception.BadRequestException;
import com.rune.security.security.AccessException;
import com.rune.security.security.AuthException;
import com.rune.security.service.AuthService;
import com.rune.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.Serializable;
import java.util.Objects;

import static java.lang.String.format;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description Security 配置
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Value("${springdoc.api-docs.path}")
    private String restApiDocPath;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain systemSecurityFilterChain(HttpSecurity http, AuthService authService) throws Exception {

        // 开启 CORS 关闭 CSRF
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        // 设置 session 为无状态
        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 设置认证相关的异常处理
        http.exceptionHandling((exceptions) -> exceptions
                .authenticationEntryPoint(new AuthException())
                .accessDeniedHandler(new AccessException()));

        // 设置页面权限
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers(format("%s/**", restApiDocPath)).permitAll()
                        .requestMatchers(format("%s/**", swaggerPath)).permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/captcha").permitAll()
                        .requestMatchers("/file/auth").permitAll()
                        .requestMatchers("/v1/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 设置 oauth2 资源服务器
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults())
                );

        // 用户登录实现
        http.userDetailsService(authService);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator());
        return expressionHandler;
    }

    /**
     * 权限评估
     *
     * @return /
     */
    @Bean
    PermissionEvaluator permissionEvaluator() {
        return new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                final String permissionCode = targetDomainObject + "_" + permission;

                if (SecurityUtils.getCurrentId() != 1L) {
                    // TODO 可以考虑从缓存从读取权限内容，这样就可以避免更新权限后，用户必须重新登录才生效的问题
                    return authentication.getAuthorities().stream().anyMatch(auth -> Objects.equals(auth.toString(), permissionCode));
                }
                return true;
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                throw new BadRequestException("暂不支持这种权限注解");
            }
        };
    }

    /*
        跨域配置
        @Bean
        public CorsFilter corsFilter() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.addAllowedOriginPattern("*");
            config.addAllowedHeader("*");
            config.addAllowedMethod("*");

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }
     */
}
