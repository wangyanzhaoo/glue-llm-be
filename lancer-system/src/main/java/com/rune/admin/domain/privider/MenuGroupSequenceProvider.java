package com.rune.admin.domain.privider;

import com.rune.admin.domain.entity.Menu;
import com.rune.exception.BadRequestException;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 大方的脑壳
 * @date 2022/9/15 12:11
 * @description 角色自定义分组校验
 */
public class MenuGroupSequenceProvider implements DefaultGroupSequenceProvider<Menu> {
    @Override
    public List<Class<?>> getValidationGroups(Menu menu) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(Menu.class);

        if (menu != null) {
            Integer type = menu.getType();
            if (type == null) {
                throw new BadRequestException("路由类型不能为空");
            }
            if (type == 0) {
                defaultGroupSequence.add(Menu.menuGroup.class);
            } else if (type == 1) {
                defaultGroupSequence.add(Menu.pathGroup.class);
            } else if (type == 2) {
                defaultGroupSequence.add(Menu.buttonGroup.class);
            }
        }
        return defaultGroupSequence;
    }
}
