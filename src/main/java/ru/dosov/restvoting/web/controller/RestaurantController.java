package ru.dosov.restvoting.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Restaurant;
import ru.dosov.restvoting.repository.RestaurantRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantRepository restRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restRepository) {
        this.restRepository = restRepository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant create(@Valid @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        return restRepository.save(restaurant);
    }

    @Cacheable("restaurants")
    @GetMapping
    public List<Restaurant> getAll() {
        return restRepository.findAll();
    }

    @Cacheable("restaurants")
    @GetMapping(value = "/menus")
    public List<Restaurant> getAllWithDishes(@RequestParam String date) {
        return restRepository.getAllRestaurantsWithDishes(LocalDate.parse(date));
    }

    @Cacheable("restaurants")
    @GetMapping(value = {"/{id}/menus"})
    public Restaurant getRestaurantWithDishes(@PathVariable Integer id, @RequestParam String date) {
        return restRepository.getRestaurantWithDishes(LocalDate.parse(date), id);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    @PutMapping(value = {"/{id}"})
    public void update(@PathVariable Integer id, @Valid @RequestBody Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        restRepository.save(restaurant);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    @DeleteMapping(value = {"/{id}"})
    public void delete(@PathVariable Integer id) {
        checkNotFound(restRepository.delete(id) != 0, id);
    }
}
