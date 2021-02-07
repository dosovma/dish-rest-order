package ru.dosov.restvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dosov.restvoting.model.Restaurant;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.to.RestaurantTo;
import ru.dosov.restvoting.util.DateTimeUtil;
import ru.dosov.restvoting.util.RestaurantUtil;
import ru.dosov.restvoting.util.exceptionhandler.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "${appattributes.baseurl}/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantController {

    private final RestaurantRepository restRepository;
    @Value(value = "${appattributes.baseurl}/restaurants")
    private String REST_URL;

    @Autowired
    public RestaurantController(RestaurantRepository restRepository) {
        this.restRepository = restRepository;
    }

    @CacheEvict(value = "restaurants", key = "'list'")
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> create(@Valid @RequestBody RestaurantTo restaurantTo) {
        checkNew(restaurantTo);
        Restaurant restaurant = new Restaurant(
                null,
                restaurantTo.getName(),
                null
        );
        RestaurantTo created = RestaurantUtil.getTo(restRepository.save(restaurant));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create new restaurant {}", restaurantTo);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Cacheable(value = "restaurants", key = "'list'")
    @GetMapping
    public List<RestaurantTo> getAllWithoutMenus() {
        log.info("get All restaurants without");
        return RestaurantUtil.getListTo(restRepository.findAll());
    }

    @Cacheable(value = "restaurants", key = "#menuDate?:'today'")
    @GetMapping(value = "/menus")
    public List<Restaurant> getAllWithMenus(@RequestParam @Nullable LocalDate menuDate) {
        log.info("get All restaurants with menus");
        LocalDate dateToMenu = DateTimeUtil.getDateOrNow(menuDate);
        return restRepository.getAllWithDishes(dateToMenu);
    }

    @GetMapping(value = "/{id}")
    public RestaurantTo getOneWithoutMenus(@PathVariable Integer id) {
        log.info("get restaurant id {} without menus", id);
        return RestaurantUtil.getTo(restRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id)));
    }

    @Cacheable(value = "restaurants", key = "#id")
    @GetMapping(value = {"/{id}/menus"})
    public Restaurant getOneWithMenus(@PathVariable Integer id, @RequestParam @Nullable LocalDate menuDate) {
        LocalDate dateToMenu = DateTimeUtil.getDateOrNow(menuDate);
        log.info("get restaurant id {} with menu", id);
        return restRepository.getOneWithDishes(dateToMenu, id).orElseThrow(() -> {
            if (restRepository.getOne(id) != null) {
                return new NotFoundException("Not found menu in this restaurant on date = " + dateToMenu);
            } else {
                return new NotFoundException("Not found restaurant with id = " + id);
            }
        });
    }

    @CacheEvict(value = "restaurants", key = "#id")
    @Transactional
    @PutMapping(value = {"/{id}"})
    public void update(@PathVariable Integer id, @Valid @RequestBody Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        log.info("update restaurant id {} to {}", id, restaurant);
        restRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", key = "#id")
    @Transactional
    @DeleteMapping(value = {"/{id}"})
    public void delete(@PathVariable Integer id) {
        log.info("delete restaurant id {}", id);
        checkNotFound(restRepository.delete(id) != 0, id);
    }
}
