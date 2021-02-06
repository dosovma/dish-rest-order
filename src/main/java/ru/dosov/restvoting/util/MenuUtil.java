package ru.dosov.restvoting.util;

import ru.dosov.restvoting.model.AbstractEntity.BaseEntity;
import ru.dosov.restvoting.model.Menu;
import ru.dosov.restvoting.to.MenuTo;

import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {
    private MenuUtil() {
    }

    public static MenuTo getTo(Menu menu) {
        return new MenuTo(
                menu.getId(),
                menu.getDate(),
                menu.getRestaurant().getId(),
                menu.getDishes().stream().map(BaseEntity::getId).collect(Collectors.toSet())
        );
    }

    public static List<MenuTo> getListTo(List<Menu> menus) {
        return menus == null
                ? List.of()
                : menus.stream().map(MenuUtil::getTo).collect(Collectors.toList());
    }
}