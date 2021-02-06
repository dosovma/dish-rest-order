package ru.dosov.restvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dosov.restvoting.model.Menu;
import ru.dosov.restvoting.repository.DishRepository;
import ru.dosov.restvoting.repository.MenuRepository;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.to.MenuTo;
import ru.dosov.restvoting.util.DateTimeUtil;
import ru.dosov.restvoting.util.exceptionhandler.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static ru.dosov.restvoting.util.MenuUtil.getListTo;
import static ru.dosov.restvoting.util.MenuUtil.getTo;
import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "${appattributes.baseurl}/menus", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MenuController {

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    @Value(value = "${appattributes.baseurl}/menus")
    private String REST_URL;

    @Autowired
    public MenuController(MenuRepository menuRepository, DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = "restaurants", key = "#menuTo.date"),
            @CacheEvict(value = "restaurants", key = "'today'")
    })
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuTo> create(@Valid @RequestBody MenuTo menuTo) {
        checkNew(menuTo);
        Menu menu = new Menu();
        menu.setDate(menuTo.getDate());
        menu.setDishes(menuTo.getDishes().stream().map(dishRepository::getOne).collect(Collectors.toSet()));
        menu.setRestaurant(restaurantRepository.getOne(menuTo.getRestaurant_id()));
        MenuTo created = getTo(menuRepository.save(menu));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create new menu {}", menuTo);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<MenuTo> getAllByRestaurant(@RequestParam @Nullable Integer restaurant, @RequestParam @Nullable LocalDate dateStart, @RequestParam @Nullable LocalDate dateEnd) {
        List<Menu> menus;
        if (dateStart == null && dateEnd == null) {
            menus = restaurant == null
                    ? menuRepository.getAllByDate(LocalDate.now())
                    : menuRepository.getAllByRestaurantAndDate(restaurant, LocalDate.now());
        } else {
            LocalDate startDate = DateTimeUtil.getOrMinDate(dateStart);
            LocalDate endDate = DateTimeUtil.getOrMaxDate(dateEnd);
            menus = restaurant == null
                    ? menuRepository.getAllBetweenDate(startDate, endDate)
                    : menuRepository.getAllByRestaurantBetweenDate(restaurant, startDate, endDate);
        }
        log.info("get menus by filtered restaurant or date");
        return getListTo(menus);
    }

    @GetMapping(value = "/{id}")
    public MenuTo getOneById(@PathVariable Integer id) {
        log.info("get menu id {}", id);
        return getTo(menuRepository.getOneById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id)));
    }


    @CacheEvict(value = "restaurants", key = "#menuTo.date")
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody MenuTo menuTo) {
        assureIdConsistent(menuTo, id);
        Menu menu = menuRepository.getOne(id);
        menu.setId(id);
        menu.setDate(menuTo.getDate());
        menu.setDishes(menuTo.getDishes().stream().map(dishRepository::getOne).collect(Collectors.toSet()));
        log.info("update menu id {} to {}", id, menuTo);
    }

    @CacheEvict(value = "restaurants", key = "#menuTo.date")
    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id) {
        log.info("delete menu id {}", id);
        checkNotFound(menuRepository.delete(id) != 0, id);
    }
}