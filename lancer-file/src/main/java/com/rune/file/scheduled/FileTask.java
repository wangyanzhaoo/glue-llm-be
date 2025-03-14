package com.rune.file.scheduled;

import com.rune.file.domain.entity.Files;
import com.rune.file.repository.FileResp;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 大方的脑壳
 * @date 2020/10/9 11:39
 * @description 文件定时任务
 */
@Component
@RequiredArgsConstructor
public class FileTask {

    private final FileResp fileResp;

    /**
     * 每周三凌晨三点删除临时文件
     */
    @Scheduled(cron = "0 0 3 ? * SAT")
    public void deleteFileBySaturday() {
        List<Files> allById = fileResp.findByStatus(true);
        Set<Long> collect = allById.stream()
                .mapToLong(Files::getId)
                .boxed()
                .collect(Collectors.toSet());
        fileResp.deleteAllByIdInBatch(collect);
    }
}
