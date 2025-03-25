package com.rune.new_api.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author avalon
 * @date 2025/3/6 16:36
 * @description 模型对话请求实体
 */
@Schema(title = "模型对话")
@Getter
@Setter
public class V1Query {

    @Schema(description = "对话消息列表")
    @NotNull(message = "对话消息列表不能为空")
    private List<Message> messages;

    @Schema(description = "是否流式输出", example = "true")
    private boolean stream;

    @Schema(description = "模型名称", example = "llama3.1:latest")
    @NotBlank(message = "模型名称不能为空")
    private String model;

    @Schema(description = "温度参数，控制生成文本的随机性", example = "0.5")
    private double temperature;

    @Schema(description = "存在惩罚，控制生成文本中重复内容的惩罚", example = "0")
    private double presencePenalty;

    @Schema(description = "频率惩罚，控制生成文本中高频词汇的惩罚", example = "0")
    private double frequencyPenalty;

    @Schema(description = "Top-p 采样参数，控制生成文本的多样性", example = "1")
    private double topP;

    @Getter
    @Setter
    public static class Message {
        @Schema(description = "消息角色", example = "user")
        @NotBlank(message = "消息角色不能为空")
        private String role;

        @Schema(description = "消息内容", example = "hi")
        @NotBlank(message = "消息内容不能为空")
        private String content;
    }

//    @NotBlank(message = "对话不能为空")
//    private String prompt;
}