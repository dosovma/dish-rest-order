package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Dish;
import ru.dosov.restvoting.repository.DishRepository;

import javax.validation.Valid;
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

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Dish create(@Valid @RequestBody Dish dish) {
        checkNew(dish);
        return dishRepository.save(dish);
    }

    @GetMapping
    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Dish getById(@PathVariable Integer id) {
        return checkNotFound(dishRepository.findById(id).orElse(null), id);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody Dish dish) {
        assureIdConsistent(dish, id);
        dishRepository.save(dish);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id) {
        checkNotFound(dishRepository.delete(id) != 0, id);
    }
}