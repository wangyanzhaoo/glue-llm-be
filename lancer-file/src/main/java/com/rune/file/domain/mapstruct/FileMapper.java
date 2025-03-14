package com.rune.file.domain.mapstruct;

import com.rune.domain.BaseMapper;
import com.rune.file.domain.entity.Files;
import com.rune.file.domain.vo.FileView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author 大方的脑壳
 * @date 2022/8/24 14:28
 * @description 文件管理 Mapstruct 转换
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper extends BaseMapper<Files, FileView> {
}
