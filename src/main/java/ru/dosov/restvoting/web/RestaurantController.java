package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Restaurant;
import ru.dosov.restvoting.repository.RestaurantRepository;

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

    @GetMapping
    public List<Restaurant> getAll() {
        return restRepository.findAll();
    }

    @GetMapping(value = "/menus")
    public List<Restaurant> getAllWithDishes(@RequestParam String date) {
        return restRepository.getAllRestaurantsWithDishes(LocalDate.parse(date));
    }

    @PostMapping
    public Restaurant create(@RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        return restRepository.save(restaurant);
    }

    @GetMapping(value = {"/{id}/menus"})
    public Restaurant getRestaurantWithDishes(@PathVariable Integer id, @RequestParam String date) {
        return restRepository.getRestaurantWithDishes(LocalDate.parse(date), id);
    }

    @PutMapping(value = {"/{id}"})
    public void update(@PathVariable Integer id, @RequestBody Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        restRepository.save(restaurant);
    }

    @DeleteMapping(value = {"/{id}"})
    public void delete(@PathVariable Integer id) {
        checkNotFound(restRepository.delete(id) != 0, id);
    }
}
