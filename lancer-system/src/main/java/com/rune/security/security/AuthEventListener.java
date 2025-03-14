package com.rune.security.security;

import com.rune.log.domain.entity.LoginLog;
import com.rune.log.service.LoginLogService;
import com.rune.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author avalon
 * @date 22/4/14 19:58
 * @description 认证监听器
 */
@RequiredArgsConstructor
@Component
public class AuthEventListener {

    private final HttpServletRequest request;

    private final LoginLogService logService;

    /**
     * 登录成功监听
     *
     * @param event /
     */
    @EventListener
    public void authSuccess(AuthenticationSuccessEvent event) {
        AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
        // 判断 token 是否第一次登录，避免每次请求都会触发记录
        if (source.getCredentials() == null) {
            LoginLog log = loginLog(source);
            log.setType(0);
            log.setMsg("登录成功");
            logService.create(log);
        }
    }

    /**
     * 登录失败监听
     *
     * @param event /
     */
    @EventListener
    public void authFailure(AuthenticationFailureBadCredentialsEvent event) {
        AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
        LoginLog log = loginLog(source);
        log.setType(1);
        log.setMsg("登录失败");
        logService.create(log);
    }

    /**
     * 注销成功监听
     *
     * @param event /
     */
    @EventListener
    public void Logout(LogoutSuccessEvent event) {
        AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
        LoginLog log = loginLog(source);
        log.setType(2);
        log.setMsg("注销成功");
        logService.create(log);
    }

    /**
     * 登录日志赋值
     *
     * @param source /
     * @return /
     */
    private LoginLog loginLog(AbstractAuthenticationToken source) {
        LoginLog log = new LoginLog();
        // TODO 未知 是为了解决认证失败无法获取的 username 的问题，需要在认证重构时用更好的方式修复
        log.setUsername(source.isAuthenticated() ? source.getName() : "未知");
        log.setIp(IpUtil.getIp(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        return log;
    }
}
