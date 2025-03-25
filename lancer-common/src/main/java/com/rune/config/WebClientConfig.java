package com.rune.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * WebClient 配置类
 * 在此类中配置 WebClient 的连接池、连接超时、响应超时等参数，
 * 以提高系统的性能和稳定性，同时避免长时间阻塞或资源耗尽。
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // 使用 Reactor Netty 的 ConnectionProvider 配置连接池
        ConnectionProvider connectionProvider = ConnectionProvider.builder("customConnectionPool")
                // 设置连接池中允许的最大连接数，根据实际负载进行调整
                .maxConnections(50)
                // 当连接池中的连接全部被占用时，允许等待获取连接的最大请求数
                .pendingAcquireMaxCount(100)
                // 设置等待获取连接的超时时间(秒)，如果超时则抛出异常
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .build();

        // 配置 HttpClient 对象，整合连接池和超时设置
        HttpClient httpClient = HttpClient.create(connectionProvider)
                // 设置 TCP 连接超时时间(毫秒)，防止长时间等待建立连接
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 设置响应超时时间(秒)
                .responseTimeout(Duration.ofSeconds(10));

        // 构建 WebClient 实例，并通过 ReactorClientHttpConnector 使用自定义的 HttpClient 配置
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}