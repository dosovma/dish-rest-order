package ru.dosov.restvoting.web;

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
@RequestMapping(value = "${appattributes.baseurl}/account")
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        checkNew(user);
        user.setRoles(EnumSet.of(Role.USER));
        User created = userRepository.save(user);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping
    public User getUserById(@ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Not found entity with id = " + userId));
    }

    @GetMapping(value = "/votes")
    public List<VoteTo> getVoteByDate(
            @RequestParam @Nullable LocalDate start,
            @RequestParam @Nullable LocalDate end,
            @ApiIgnore @AuthenticationPrincipal AuthUser authUser
    ) {
        LocalDate startDay = DateTimeUtil.getOrMinDate(start);
        LocalDate endDay = DateTimeUtil.getOrMaxDate(end);
        List<Vote> votes = voteRepository.getAllByUserOrDate(authUser.id(), startDay, endDay);
        return getListTo(votes);
    }

    @GetMapping(value = "/votes/{vote_id}")
    public VoteTo getVoteById(@PathVariable("vote_id") Integer vote_id, @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        Vote vote = voteRepository.getVoteByIdAndUser(userId, vote_id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + userId));
        return VoteUtil.getTo(vote);
    }

    //TODO Check encode password
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
        userRepository.save(user);
    }

    @Transactional
    @DeleteMapping
    public void delete(@ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        checkNotFound(userRepository.delete(userId) != 0, userId);
    }
}