package ru.dosov.restvoting.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.dosov.restvoting.config.AppConfig;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.RestaurantRepository;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;
import static ru.dosov.restvoting.util.VoteUtil.getListTo;
import static ru.dosov.restvoting.util.VoteUtil.getTo;

@RestController
@RequestMapping(value = "${appattributes.baseurl}/votes", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteController {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    @Value(value = "${appattributes.baseurl}/votes")
    private String REST_URL;

    @Autowired
    public VoteController(VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> create(@Valid @RequestBody VoteTo voteTo, @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        checkNew(voteTo);
        Vote vote = new Vote(
                null,
                LocalDate.now(),
                authUser.getUser(),
                restaurantRepository.getOne(voteTo.getRestaurant_id())
        );
        Vote savedVote = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(savedVote.getId()).toUri();
        log.info("create new vote {} by user id {}", vote, authUser.id());
        return ResponseEntity.created(uriOfNewResource).body(getTo(savedVote));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping
    public List<VoteTo> getAllByUserAndDate(
            @RequestParam @Nullable Integer user,
            @RequestParam @Nullable LocalDate start,
            @RequestParam @Nullable LocalDate end
    ) {
        LocalDate startDay = DateTimeUtil.getOrMinDate(start);
        LocalDate endDay = DateTimeUtil.getOrMaxDate(end);
        List<Vote> votes = user == null
                ? voteRepository.getAllByDate(startDay, endDay)
                : voteRepository.getAllByUserOrDate(user, startDay, endDay);
        log.info("get vote history filtered by user or dates");
        return getListTo(votes);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping(value = "/{id}")
    public VoteTo getOneById(@PathVariable Integer id) {
        Vote vote = voteRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
        log.info("get vote id {}", id);
        return VoteUtil.getTo(vote);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody VoteTo voteTo, @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        assureIdConsistent(voteTo, id);
        Vote vote = voteRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
        checkPermissionToWorkWithVote(vote, authUser);
        checkVoteTime(LocalDateTime.of(vote.getVoteDate(), LocalTime.now()), AppConfig.DEAD_LINE);
        vote.setRestaurant(restaurantRepository.getOne(voteTo.getRestaurant_id()));
        log.info("update vote id {} to {} by user id {}", id, voteTo, authUser.id());
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Integer id, @ApiIgnore @AuthenticationPrincipal AuthUser authUser) {
        Vote vote = voteRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found entity with id = " + id));
        checkPermissionToWorkWithVote(vote, authUser);
        checkVoteTime(LocalDateTime.of(vote.getVoteDate(), LocalTime.now()), AppConfig.DEAD_LINE);
        log.info("delete vote id {}", id);
        voteRepository.delete(id);
    }
}