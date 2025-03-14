package com.rune;

import com.rune.llm.domain.entity.ApiToken;
import com.rune.llm.domain.entity.Channel;
import com.rune.llm.service.ApiTokenService;
import com.rune.llm.service.ChannelService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * @author yangkf
 * @date 2025/3/14 16:47
 * @description
 */
@SpringBootTest
public class 测试新增 {

    @Resource
    ApiTokenService apiTokenService;

    @Resource
    ChannelService channelService;

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
    }

    @Test
    public void test1() {
        ApiToken apiToken = new ApiToken();
        apiToken.setGroup("default");
        apiToken.setName("name");
        apiToken.setUnlimit(false);
        apiToken.setModelLimit(false);
        apiToken.setStatus(1L);
        apiToken.setExpireDate(-1L);
        apiToken.setGroup("default");
        apiTokenService.create(apiToken);
    }

    @Test
    public void test12() {
        Channel channel = new Channel();
        channel.setGroup("default");
        channel.setName("name");
        channel.setType(1L);
        channel.setStatus(1L);
        channel.setWeight(0L);
        channel.setPriority(0L);
        channel.setAutoBan(1L);
        channel.setModels("gpt-3.5-turbo,gpt-3.5-turbo-0613,gpt-3.5-turbo-1106,gpt-3.5-turbo-0125,gpt-3.5-turbo-16k,gpt-3.5-turbo-16k-0613,gpt-3.5-turbo-instruct,gpt-4,gpt-4-0613,gpt-4-1106-preview,gpt-4-0125-preview,gpt-4-32k,gpt-4-32k-0613,gpt-4-turbo-preview,gpt-4-turbo,gpt-4-turbo-2024-04-09,gpt-4-vision-preview,chatgpt-4o-latest,gpt-4o,gpt-4o-2024-05-13,gpt-4o-2024-08-06,gpt-4o-2024-11-20,gpt-4o-mini,gpt-4o-mini-2024-07-18,gpt-4.5-preview,gpt-4.5-preview-2025-02-27,o1-preview,o1-preview-2024-09-12,o1-mini,o1-mini-2024-09-12,o3-mini,o3-mini-2025-01-31,o3-mini-high,o3-mini-2025-01-31-high,o3-mini-low,o3-mini-2025-01-31-low,o3-mini-medium,o3-mini-2025-01-31-medium,o1,o1-2024-12-17,gpt-4o-audio-preview,gpt-4o-audio-preview-2024-10-01,gpt-4o-realtime-preview,gpt-4o-realtime-preview-2024-10-01,gpt-4o-realtime-preview-2024-12-17,gpt-4o-mini-realtime-preview,gpt-4o-mini-realtime-preview-2024-12-17,text-embedding-ada-002,text-embedding-3-small,text-embedding-3-large,text-curie-001,text-babbage-001,text-ada-001,text-moderation-latest,text-moderation-stable,text-davinci-edit-001,davinci-002,babbage-002,dall-e-3,whisper-1,tts-1,tts-1-1106,tts-1-hd,tts-1-hd-1106");
        channelService.create(channel);
    }

}
