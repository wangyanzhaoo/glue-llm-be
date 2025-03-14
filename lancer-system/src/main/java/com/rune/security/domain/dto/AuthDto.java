package com.rune.security.domain.dto;

import com.rune.admin.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description /
 */
@Schema(title = "认证 DTO")
@RequiredArgsConstructor
public class AuthDto implements UserDetails, Serializable {

    private final User user;

    private final Collection<? extends GrantedAuthority> authorities;

    public Long getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * 权限
     *
     * @return /
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 账号是否过期，默认false
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否锁定，默认false
     *
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账号凭证是否未过期，默认false
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账号凭证是否禁用，默认false
     *
     * @return /
     */
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
