package ru.dosov.restvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.repository.VoteRepository;
import ru.dosov.restvoting.to.VoteTo;
import ru.dosov.restvoting.util.AuthUser;
import ru.dosov.restvoting.util.DateTimeUtil;
import ru.dosov.restvoting.util.VoteUtil;
import ru.dosov.restvoting.util.exceptionhandler.exception.NotFoundException;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;
import static ru.dosov.restvoting.util.VoteUtil.getListTo;

@RestController
@RequestMapping(value = "${appattributes.baseurl}/account", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AccountController {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Value(value = "${appattributes.baseurl}/account")
    private String REST_URL;

    @Autowired
    public AccountController(UserRepository userRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @Transactional
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        checkNew(user);
        user.setRoles(EnumSet.of(Role.USER));
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create new user {}", user);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public User getOneById(@ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int id = authUser.id();
        log.info("user id {} get details", id);
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
    }

    @GetMapping(value = "/votes")
    public List<VoteTo> getVoteByDate(
            @RequestParam @Nullable LocalDate start,
            @RequestParam @Nullable LocalDate end,
            @ApiIgnore @AuthenticationPrincipal AuthUser authUser
    ) {
        int id = authUser.id();
        List<Vote> votes;
        if (start == null && end == null) {
            votes = voteRepository.getOneByDate(id, LocalDate.now());
        } else {
            LocalDate startDay = DateTimeUtil.getOrMinDate(start);
            LocalDate endDay = DateTimeUtil.getOrMaxDate(end);
            votes = voteRepository.getAllByUserOrDate(id, startDay, endDay);
        }
        log.info("user id {} get own vote's history", id);
        return getListTo(votes);
    }

    @GetMapping(value = "/votes/{vote_id}")
    public VoteTo getVoteById(@PathVariable("vote_id") Integer vote_id, @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        Vote vote = voteRepository.getOneByIdAndUser(userId, vote_id).orElseThrow(() -> {
            if (userRepository.getOne(userId) != null) {
                return new NotFoundException("Not found user with id = " + userId);
            } else {
                return new NotFoundException("Not found vote with id = " + vote_id);
            }
        });
        log.info("user id {} get own vote by id {}", userId, vote_id);
        return VoteUtil.getTo(vote);
    }

    @Transactional
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody User user, @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        assureIdConsistent(user, userId);
        User oldUser = authUser.getUser();
        user.setRoles(oldUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        }
        log.info("update user id {} to {}", userId, user);
        userRepository.save(user);
    }

    @Transactional
    @DeleteMapping
    public void delete(@ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("delete user id {}", userId);
        checkNotFound(userRepository.delete(userId) != 0, userId);
    }
}