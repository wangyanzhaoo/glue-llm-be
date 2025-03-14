package com.rune.admin.service;

import com.rune.admin.domain.entity.Menu;
import com.rune.admin.domain.mapstruct.MenuMapper;
import com.rune.admin.domain.vo.MenuView;
import com.rune.admin.repository.MenuRepo;
import com.rune.exception.BadRequestException;
import com.rune.utils.UpdateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author avalon
 * @date 22/7/14 15:04
 * @description 菜单接口实现
 */
@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepo menuRepo;

    private final MenuMapper menuMapper;

    public List<MenuView> queryAll() {
        return menuMapper.toVo(menuRepo.findAll(Sort.by(Sort.Direction.ASC, "sort")));
    }

    public List<MenuView> tree() {
        return menuMapper.toVo(menuRepo.findAll(Sort.by(Sort.Direction.ASC, "sort")));
    }

    public void create(Menu resources) {
        menuRepo.save(resources);
    }

    public void update(Menu resources) {
        Menu menu = menuRepo.findById(resources.getId()).orElseThrow(() -> new BadRequestException("菜单不存在"));
        resources.setPid(null);
        UpdateUtil.copyNullProperties(menu, resources);
        menuRepo.save(resources);
    }
    
    public void delete(Long id) {
        Menu menu = menuRepo.findById(id).orElseThrow(() -> new BadRequestException("菜单不存在"));
        if (menu.getType() != 2 && menuRepo.countByPid(menu.getId()) > 0)
            throw new BadRequestException("存在子项数据，无法删除");
        //是否被角色使用
        if (menuRepo.countByMenuId(id) > 0)
            throw new BadRequestException("菜单被使用");
        menuRepo.deleteById(id);
    }

}
