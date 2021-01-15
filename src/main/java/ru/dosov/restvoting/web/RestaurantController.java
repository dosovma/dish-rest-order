package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.dosov.restvoting.model.Restaurant;
import ru.dosov.restvoting.repository.RestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/restaurants")
public class RestaurantController {

    private RestaurantRepository restRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restRepository) {
        this.restRepository = restRepository;
    }

    @GetMapping
    public List<Restaurant> getAll(@RequestParam String date) {
        return restRepository.getAllRestaurantsWithDishes(LocalDate.parse(date));
    }

}
