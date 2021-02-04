package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dosov.restvoting.config.WebSecurityConfig;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.util.exceptionhandler.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "${appattributes.baseurl}/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;
    @Value(value = "${appattributes.baseurl}/admin/users")
    private String REST_URL;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO do @validated password length using databinder
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        checkNew(user);
        if (user.getRoles() == null) {
            user.setRoles(EnumSet.of(Role.USER));
        }
        User created = userRepository.save(user);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Cacheable(value = "users")
    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public User getById(@PathVariable Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody User user) {
        assureIdConsistent(user, id);
        User userFromDB = userRepository.getOne(id);
        userFromDB.setEmail(user.getEmail());
        userFromDB.setName(user.getName());
        String password = user.getPassword();
        if (StringUtils.hasText(password)) {
            userFromDB.setPassword(WebSecurityConfig.PASSWORD_ENCODER.encode(password));
        }
        userFromDB.setRoles(user.getRoles());
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id) {
        checkNotFound(userRepository.delete(id) != 0, id);
    }
}
