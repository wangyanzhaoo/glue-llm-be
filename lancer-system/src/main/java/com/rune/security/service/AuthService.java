package com.rune.security.service;

import com.rune.admin.domain.entity.Menu;
import com.rune.admin.domain.entity.Role;
import com.rune.admin.domain.entity.User;
import com.rune.admin.repository.UserRepo;
import com.rune.security.domain.dto.AuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description 认证接口实现
 */
@RequiredArgsConstructor
@Service
public class AuthService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // XXX 这里的抛去用户名不存在异常，会被 BadCredentialsException 捕获，因此写的错误内容无法提示到前端
        User user = userRepo.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("/")
        );

        // 获取权限
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        /*
          XXX 这里应该是 如果是管理员就不获取权限，但是因为前端 access 无法写多个标识符
           PreAuthorize 里写了是管理员就全部放开
         */
        for (Role role : user.getRoles()) {
            for (Menu menu : role.getMenus()) {
                if (menu.getType() == 2) {
                    authorities.add(new SimpleGrantedAuthority(menu.getAuth()));
                }
            }
        }

        return new AuthDto(user, authorities);
    }

}
