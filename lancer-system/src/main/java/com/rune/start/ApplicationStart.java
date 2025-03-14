package com.rune.start;

import com.rune.admin.service.DictService;
import com.rune.config.service.ConfigService;
import com.rune.utils.RedisUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author sedate
 * @date 2023/6/30 15:22
 * @description 启动项目刷新缓存
 */
@Component
@RequiredArgsConstructor
public class ApplicationStart implements ApplicationRunner {

    private final DictService dictService;

    private final ConfigService configService;

    private final RedisUtils redisUtils;

    @Override
    public void run(ApplicationArguments args){
        // 删除旧缓存
        redisUtils.delete("dict::all");
        redisUtils.delete("config::all");
        // 加入新缓存
        dictService.data();
        configService.data();
    }
}
