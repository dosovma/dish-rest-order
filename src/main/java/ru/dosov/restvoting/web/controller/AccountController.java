package ru.dosov.restvoting.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.web.AuthUser;

import java.util.EnumSet;

@RestController
@RequestMapping(value = "/api/v1/account")
public class AccountController {

    private final UserRepository userRepository;

    @Autowired
    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setRoles(EnumSet.of(Role.USER));
        return userRepository.save(user);
    }

    //TODO проверка разрешения удаления
    @DeleteMapping(value = "/{id}")
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable("id") Integer id) {
        userRepository.deleteById(authUser.id());
    }
}