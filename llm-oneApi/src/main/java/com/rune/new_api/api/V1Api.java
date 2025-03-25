package com.rune.new_api.api;

import com.rune.new_api.domain.dto.V1Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
/**
 * LLM 请求代理控制器
 * 负责处理客户端聊天请求，转发到 OneAPI 服务，并进行内容过滤
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class V1Api {

    // 从配置文件注入 API 地址
    @Value("${oneapi.url}")
    private String oneApiUrl;

    // 从配置文件注入 API 密钥
    @Value("${oneapi.key}")
    private String apiKey;

    private final WebClient webClient;

    // 系统常量定义
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);
    private static final String DEFAULT_MODEL = "llama3.1:latest";

    // 内容过滤规则配置
    private static final Pattern SENSITIVE_PATTERN = Pattern.compile(
            "(?i)(违禁词1|违禁词2|敏感词)"  // 在这里添加需要过滤的词汇
    );
    private static final String REPLACEMENT_TEXT = "[内容已过滤]";

    /**
     * 处理聊天完成请求的端点
     * 接收客户端请求，转发到 OneAPI，并返回过滤后的流式响应
     *
     * @param chatQuery 包含用户提示的查询对象
     * @return 经过过滤的 SSE 流式响应
     */
    @PostMapping(value = "/chat/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> completionsProxy(@Validated @RequestBody V1Query chatQuery) {
        // 记录输入的提示，使用 debug 级别避免敏感信息泄露
        log.debug("Received streaming prompt: {}", chatQuery.getMessages());

        // 检查输入内容是否包含敏感词
//        String filteredPrompt = filterContent(chatQuery.getMessages());

        // 构建消息体
        final var messages = chatQuery.getMessages();

        // 构建请求参数
        final var params = Map.of(
                "model", DEFAULT_MODEL,
                "stream", true,
                "temperature", 0.5,
                "presence_penalty", 0,
                "frequency_penalty", 0,
                "top_p", 1,
                "messages", messages,
                "max_tokens", 2000  // 限制响应长度
        );

        return webClient.post()
                .uri(oneApiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .bodyValue(params)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleClientError)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerError)
                .bodyToFlux(String.class)
                .timeout(REQUEST_TIMEOUT)
                .map(this::filterAndCreateServerSentEvent)  // 过滤响应内容
                .onErrorResume(this::handleError)
                .doOnComplete(() -> log.debug("Stream completed for prompt: {}", chatQuery.getMessages()));
    }

    /**
     * 处理客户端错误（4xx）
     */
    private Mono<? extends Throwable> handleClientError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    log.error("Client error while forwarding request: {}", errorBody);
                    return Mono.error(new ApiException("请求参数错误，请检查输入"));
                });
    }

    /**
     * 处理服务器错误（5xx）
     */
    private Mono<? extends Throwable> handleServerError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(errorBody -> {
                    log.error("Downstream service error: {}", errorBody);
                    return Mono.error(new ApiException("服务暂时不可用，请稍后重试"));
                });
    }

    /**
     * 创建 SSE 事件，并在发送前过滤内容
     */
    private ServerSentEvent<String> filterAndCreateServerSentEvent(String data) {
        String filteredData = filterContent(data);
        return ServerSentEvent.builder(filteredData)
                .id(UUID.randomUUID().toString())
                .build();
    }

    /**
     * 统一的错误处理方法
     */
    private Flux<ServerSentEvent<String>> handleError(Throwable ex) {
        log.error("Error during streaming forwarding: {}", ex.getMessage(), ex);
        String errorMessage = (ex instanceof ApiException) ?
                ex.getMessage() : "处理请求时发生错误，请稍后再试";
        return Flux.just(ServerSentEvent.builder(errorMessage).build());
    }

    /**
     * 内容过滤方法
     * 将敏感内容替换为指定文本
     *
     * @param content 需要过滤的内容
     * @return 过滤后的内容
     */
    private String filterContent(String content) {
        if (content == null) {
            return "";
        }

        // 使用正则表达式匹配和替换敏感内容
        String filtered = SENSITIVE_PATTERN.matcher(content).replaceAll(REPLACEMENT_TEXT);

        // 如果内容被修改，记录日志
        if (!filtered.equals(content)) {
            log.warn("Content filtered: sensitive content detected and replaced");
        }

        return filtered;
    }
}

/**
 * 自定义API异常类
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}