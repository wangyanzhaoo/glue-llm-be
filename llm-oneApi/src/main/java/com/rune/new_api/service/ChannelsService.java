package com.rune.new_api.service;

import com.rune.exception.BadRequestException;
import com.rune.new_api.domain.dto.ChannelsQuery;
import com.rune.new_api.domain.entity.Channels;
import com.rune.new_api.domain.mapstruct.ChannelsMapper;
import com.rune.new_api.domain.vo.ChannelsView;
import com.rune.new_api.repository.ChannelsRepo;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description new-api 渠道表接口实现
 */
@Service
@RequiredArgsConstructor
public class ChannelsService {

    private final ChannelsRepo channelsRepo;

    private final ChannelsMapper channelsMapper;

    public Page<ChannelsView> queryAll(ChannelsQuery query) {
        Page<Channels> page = channelsRepo.findAll(PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(channelsMapper::toVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Channels resources) {
        channelsRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Channels resources) {
        Channels channels = channelsRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("更新的new-api 渠道表不存在"));
        UpdateUtil.copyNullProperties(channels, resources);
        channelsRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        channelsRepo.deleteAllByIdIn(ids);
    }
}
