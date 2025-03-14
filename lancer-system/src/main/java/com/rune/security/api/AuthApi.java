package com.rune.security.api;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import com.rune.admin.service.UserService;
import com.rune.admin.util.DataRuleUtil;
import com.rune.domain.ApiOk;
import com.rune.exception.BadRequestException;
import com.rune.security.domain.dto.AuthDto;
import com.rune.security.domain.dto.AuthRequest;
import com.rune.utils.ApiResponse;
import com.rune.utils.RedisUtils;
import com.rune.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author avalon
 * @date 22/3/30 19:38
 * @description /
 */
@Tag(name = "认证 Api")
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthApi {

    private final AuthenticationManager authenticationManager;

    private final JwtEncoder jwtEncoder;

    private final RedisUtils redisUtils;

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseEntity<ApiOk> login(@Validated @RequestBody AuthRequest request) {

        switch (redisUtils.get("captcha:" + request.getUuid())) {
            case null -> throw new BadRequestException("验证码已过期");
            case String capt when !capt.equals(request.getCaptcha()) ->
                    throw new BadRequestException("验证码输入错误");
            default -> {}
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        AuthDto authDto = (AuthDto) authentication.getPrincipal();

        // 保持登录是 12小时, 否则 6小时
        long expiry = request.getAutoLogin() ? 43200L : 21600L;

        // XXX 转换权限的格式，反序列化时默认读取权限用的分隔符是空格
        String auth = authDto.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .id(authDto.getId().toString())
                .subject(authDto.getUsername())
                .claim("auth", auth)
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        // 加入缓存
        redisUtils.set("token:" + authDto.getUsername(), token, expiry);

        record AuthInfo(String token, String user) {}
        AuthInfo authInfo = new AuthInfo(token, authDto.getUsername());
        return ApiResponse.ok(authInfo);
    }

    @Operation(summary = "注销")
    @DeleteMapping("/logout")
    public ResponseEntity<ApiOk> logout() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        publisher.publishEvent(new LogoutSuccessEvent(authentication));
        redisUtils.delete("token:" + SecurityUtils.getCurrentUsername());
        return ApiResponse.ok();
    }


    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public ResponseEntity<ApiOk> captcha() {

        String uuid = UUID.randomUUID().toString();
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(98, 38, 0, 0);
        captcha.setGenerator(randomGenerator);

        redisUtils.set("captcha:" + uuid, captcha.getCode(), 60);
        record ImgResult(String uuid, String img) {}
        ImgResult imgResult = new ImgResult(uuid, captcha.getImageBase64Data());
        return ApiResponse.ok(imgResult);
    }


    @Operation(summary = "导航栏")
    @GetMapping("/nav")
    public ResponseEntity<ApiOk> nav() {
        // 获取redis中数据，没有则重新生成
        Object o = redisUtils.get("info:" + SecurityUtils.getCurrentUsername());
        if (Objects.isNull(o))
            DataRuleUtil.getAllowUsername(Set.of(SecurityUtils.getCurrentId()));
        return ApiResponse.ok(userService.userInfo(SecurityUtils.getCurrentUsername()));
    }
}
