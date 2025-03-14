package com.rune.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author sedate
 * @date 2024/1/4 14:43
 * @description RestTemplate 配置
 */
@Configuration
public class RestConfig {

    /**
     * 生成全局的 RestTemplate 对象
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}