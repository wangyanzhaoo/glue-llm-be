package com.rune.llm.service;

import com.rune.exception.BadRequestException;
import com.rune.llm.domain.dto.ChannelQuery;
import com.rune.llm.domain.entity.Channel;
import com.rune.llm.domain.entity.QChannel;
import com.rune.llm.domain.mapstruct.ChannelMapper;
import com.rune.llm.domain.vo.ChannelView;
import com.rune.llm.repository.ChannelRepo;
import com.rune.new_api.domain.entity.Channels;
import com.rune.new_api.repository.ChannelsRepo;
import com.rune.querydsl.JPAQueryFactoryPrimary;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

/**
 * @author yangkf
 * @date 2025-03-14
 * @description 渠道表接口实现
 */
@Service
@RequiredArgsConstructor
public class ChannelService {

    final ChannelsRepo apiChannelsRepo;
    final JPAQueryFactoryPrimary queryFactory;
    private final ChannelRepo channelRepo;
    private final ChannelMapper channelMapper;

    public Page<ChannelView> queryAll(ChannelQuery query) {
        Page<Channel> page = channelRepo.findAll(PageRequest.of(query.getCurrent(), query.getPageSize()));
        return page.map(channelMapper::toVo);
    }

    @Transactional(value = "unionTransactionManager", rollbackFor = Exception.class)
    public void create(Channel resources) {
        resources.setKey(UUID.randomUUID().toString());
        channelRepo.save(resources);
        if (StringUtils.isBlank(resources.getUsedLimit())) {
            resources.setUsedLimit("0");
        }
        if (StringUtils.isBlank(resources.getRemainLimit())) {
            resources.setRemainLimit("0");
        }
        fillNewApiChannelEntity(resources);
    }

    private void fillNewApiChannelEntity(Channel resources) {
        Channels channels = new Channels();
        channels.setGroup(resources.getGroup());
        channels.setName(resources.getName());
        channels.setKey(resources.getKey());
        channels.setType(resources.getType());
//        channels.setOpenAiOrganization("");
//        channels.setTestModel("");
        channels.setStatus(resources.getStatus());
        channels.setWeight(resources.getWeight());
        channels.setResponseTime(resources.getResponseTime());
        channels.setUsedQuota(Long.parseLong(resources.getUsedLimit()));
        channels.setBalance(Double.valueOf(resources.getRemainLimit()));
        channels.setBalanceUpdatedTime(resources.getRemainLimitUpdateTime());
        channels.setModels(resources.getModels());
        channels.setPriority(resources.getPriority());
        channels.setAutoBan(resources.getAutoBan());
        apiChannelsRepo.save(channels);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Channel resources) {
        Channel channel = channelRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("更新的渠道表不存在"));
        UpdateUtil.copyNullProperties(channel, resources);
        channelRepo.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        QChannel qChannel = QChannel.channel;
        queryFactory.update(qChannel).set(qChannel.isDelete, true).where(qChannel.id.in(ids)).execute();
    }
}
