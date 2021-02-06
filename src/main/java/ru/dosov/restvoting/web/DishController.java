package ru.dosov.restvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dosov.restvoting.model.Dish;
import ru.dosov.restvoting.repository.DishRepository;
import ru.dosov.restvoting.util.exceptionhandler.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.assureIdConsistent;
import static ru.dosov.restvoting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = "${appattributes.baseurl}/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DishController {

    private final DishRepository dishRepository;
    @Value(value = "${appattributes.baseurl}/dishes")
    private String REST_URL;

    @Autowired
    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@Valid @RequestBody Dish dish) {
        checkNew(dish);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create new dish {}", dish);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<Dish> getAll() {
        log.info("get all dishes");
        return dishRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Dish getOneById(@PathVariable Integer id) {
        log.info("get dish id {}", id);
        return dishRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody Dish dish) {
        assureIdConsistent(dish, id);
        log.info("update dish id {} to {}", id, dish);
        dishRepository.save(dish);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
        dish.setEnable(false);
        log.info("delete dish id {}", id);
    }
}