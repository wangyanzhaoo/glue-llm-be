package com.rune.domain;

import java.util.List;
import java.util.Set;

/**
 * @author avalon
 * @date 22/3/30 18:28
 * @description Mapstruct 基础转换接口
 */
public interface BaseMapper<E, V> {

    /**
     * Entity 转 Vo
     *
     * @param entity /
     * @return /
     */
    V toVo(E entity);

    E toEntity(V vo);

    List<V> toVo(List<E> entityList);

    List<E> toEntity(List<V> voList);

    Set<V> toVo(Set<E> entityList);

    Set<E> toEntity(Set<V> voList);
}
