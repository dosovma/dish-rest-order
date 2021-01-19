package ru.dosov.restvoting.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Menu;
import ru.dosov.restvoting.repository.MenuRepository;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.to.MenuTo;
import ru.dosov.restvoting.util.DateTimeUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static ru.dosov.restvoting.util.MenuUtil.getListTo;
import static ru.dosov.restvoting.util.MenuUtil.getTo;
import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/api/v1/menus")
public class MenuController {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuController(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public MenuTo create(@Valid @RequestBody MenuTo menuTo) {
        checkNew(menuTo);
        Menu menu = new Menu();
        menu.setDate(menuTo.getDate());
        menu.setDishes(menuTo.getDishes());
        menu.setRestaurant(restaurantRepository.getOne(menuTo.getRestaurant_id()));
        return getTo(menuRepository.save(menu));
    }

    @GetMapping
    public List<MenuTo> getAllByRestaurant(@RequestParam @Nullable Integer restaurant, @RequestParam @Nullable LocalDate dateStart, @RequestParam @Nullable LocalDate dateEnd) {
        LocalDate startDate = DateTimeUtil.startLocalDayOrMin(dateStart);
        LocalDate endDate = DateTimeUtil.startLocalDayOrMin(dateEnd);
        List<Menu> menus;
        if (startDate == null && endDate == null) {
            menus = restaurant == null
                    ? menuRepository.getMenuByDate(LocalDate.now())
                    : menuRepository.getMenuByRestaurantAndDate(restaurant, LocalDate.now());
        } else {
            menus = restaurant == null
                    ? menuRepository.getMenuBetweenDate(startDate, endDate)
                    : menuRepository.getMenuByRestaurantBetweenDate(restaurant, startDate, endDate);
        }
        return getListTo(menus);
    }

    @GetMapping(value = "/{id}")
    public MenuTo getById(@PathVariable Integer id) {
        return getTo(checkNotFound(menuRepository.findById(id).orElse(null), id));
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody MenuTo menuTo) {
        assureIdConsistent(menuTo, id);
        Menu menu = new Menu();
        menu.setId(id);
        menu.setDate(menuTo.getDate());
        menu.setDishes(menuTo.getDishes());
        menu.setRestaurant(restaurantRepository.getOne(menuTo.getRestaurant_id()));
        menuRepository.save(menu);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id) {
        checkNotFound(menuRepository.delete(id) != 0, id);
    }
}