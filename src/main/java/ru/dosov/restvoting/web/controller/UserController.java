package ru.dosov.restvoting.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.repository.UserRepository;

import javax.validation.Valid;
import java.util.EnumSet;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/api/v1/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@Valid @RequestBody User user) {
        checkNew(user);
        if (user.getRoles() == null) {
            user.setRoles(EnumSet.of(Role.USER));
        }
        return userRepository.save(user);
    }

    @Cacheable(value = "users")
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Cacheable("users")
    @GetMapping(value = "/{id}")
    public User getById(@PathVariable Integer id) {
        return checkNotFound(userRepository.findById(id).orElse(null), id);
    }

    //TODO do update without password in body
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody User user) {
        assureIdConsistent(user, id);
        userRepository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id) {
        checkNotFound(userRepository.delete(id) != 0, id);
    }
}
