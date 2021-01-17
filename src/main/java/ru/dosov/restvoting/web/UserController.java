package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.repository.UserRepository;

import java.util.EnumSet;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;


    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody User user) {
        checkNew(user);
        user.setRoles(EnumSet.of(Role.USER));
        return userRepository.save(user);
    }

    @GetMapping(value = "/{id}")
    public User getById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, id);
        return checkNotFound(userRepository.findById(id).orElse(null), id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, id);
        assureIdConsistent(user, id);
        userRepository.save(user);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, id);
        checkNotFound(userRepository.delete(id) != 0, id);
    }
}
