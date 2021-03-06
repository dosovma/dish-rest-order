package ru.dosov.restvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    @Value(value = "${appattributes.baseurl}/admin/users")
    private String REST_URL;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        log.info("create new user {} by admin", user);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("get all users by admin");
        return userRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public User getOneById(@PathVariable Integer id) {
        log.info("get user id {} by admin", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody User user) {
        assureIdConsistent(user, id);
        log.info("update user id {} to {} by admin", id, user);
        User userFromDB = userRepository.getOne(id);
        userFromDB.setEmail(user.getEmail());
        userFromDB.setName(user.getName());
        String password = user.getPassword();
        if (StringUtils.hasText(password)) {
            userFromDB.setPassword(WebSecurityConfig.PASSWORD_ENCODER.encode(password));
        }
        userFromDB.setRoles(user.getRoles());
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id) {
        log.info("delete user id {} by admin", id);
        checkNotFound(userRepository.delete(id) != 0, id);
    }
}
