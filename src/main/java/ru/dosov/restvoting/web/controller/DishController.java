package ru.dosov.restvoting.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Dish;
import ru.dosov.restvoting.repository.DishRepository;

import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/api/v1/dishes")
public class DishController {

    private final DishRepository dishRepository;

    @Autowired
    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish create(@RequestBody Dish dish) {
        checkNew(dish);
        return dishRepository.save(dish);
    }

    @GetMapping(value = "/{id}")
    public Dish getById(@PathVariable Integer id) {
        return checkNotFound(dishRepository.findById(id).orElse(null), id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @RequestBody Dish dish) {
        assureIdConsistent(dish, id);
        dishRepository.save(dish);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id) {
        checkNotFound(dishRepository.delete(id) != 0, id);
    }
}