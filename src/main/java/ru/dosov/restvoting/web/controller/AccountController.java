package ru.dosov.restvoting.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.model.Role;
import ru.dosov.restvoting.model.User;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.repository.VoteRepository;
import ru.dosov.restvoting.to.VoteTo;
import ru.dosov.restvoting.util.AuthUser;
import ru.dosov.restvoting.util.DateTimeUtil;
import ru.dosov.restvoting.util.VoteUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;
import static ru.dosov.restvoting.util.VoteUtil.getListTo;

@RestController
@RequestMapping(value = "/api/v1/account")
public class AccountController {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public AccountController(UserRepository userRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @CacheEvict(value = "account", allEntries = true)
    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@Valid @RequestBody User user) {
        checkNew(user);
        user.setRoles(EnumSet.of(Role.USER));
        return userRepository.save(user);
    }

    @Cacheable("account")
    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, id);
        return checkNotFound(userRepository.findById(id).orElse(null), id);
    }

    @Cacheable("account")
    @GetMapping(value = "/{id}/votes")
    public List<VoteTo> getVoteByDate(
            @PathVariable Integer id,
            @RequestParam @Nullable LocalDate start,
            @RequestParam @Nullable LocalDate end,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        checkPermission(authUser, id);
        LocalDateTime startDay = DateTimeUtil.getDateTimeOrMin(start);
        LocalDateTime endDay = DateTimeUtil.getDateTimeOrMax(end);
        List<Vote> votes = voteRepository.getAllByUserOrDate(id, startDay, endDay);
        return getListTo(votes);
    }

    @Cacheable("account")
    @GetMapping(value = "/{user_id}/votes/{vote_id}")
    public VoteTo getVoteById(@PathVariable("vote_id") Integer vote_id, @PathVariable("user_id") Integer id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, id);
        Vote vote = checkNotFound(voteRepository.getVoteByIdAndUser(id, vote_id).orElse(null), vote_id);
        return VoteUtil.getTo(vote);
    }

    //TODO Check encode password
    @CacheEvict(value = "account", allEntries = true)
    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody User user, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, id);
        assureIdConsistent(user, id);
        User oldUser = authUser.getUser();
        user.setRoles(oldUser.getRoles());
        if (user.getPassword() == null) {
            user.setPassword(oldUser.getPassword());
        }
//        String password = user.getPassword();
//        if (StringUtils.hasText(password)) {
//            user.setPassword(WebSecurityConfig.PASSWORD_ENCODER.encode(password));
//        }
//        user.setRoles(EnumSet.of(Role.USER));
        userRepository.save(user);
    }

    @CacheEvict(value = "account", allEntries = true)
    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") Integer id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, id);
        checkNotFound(userRepository.delete(id) != 0, id);
    }
}