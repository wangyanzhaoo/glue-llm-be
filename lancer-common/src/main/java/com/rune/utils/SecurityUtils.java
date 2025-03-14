package com.rune.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author avalon
 * @date 22/3/30 18:28
 * @description Security工具 用于从jwt中读取基本用户信息
 */
@Component
public class SecurityUtils {

    private static ObjectMapper objectMapper;

    public static Long getCurrentId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ObjectNode principal = objectMapper.convertValue(authentication.getPrincipal(), ObjectNode.class);
        ObjectNode claims = objectMapper.convertValue(principal.get("claims"), ObjectNode.class);
        return Long.valueOf(claims.get("jti").asText());
    }

    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static Set<String> getCurrentAuth() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    /**
     * 静态注入对象
     */
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        SecurityUtils.objectMapper = objectMapper;
    }
}
