package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.config.AppConfig;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.repository.VoteRepository;
import ru.dosov.restvoting.to.VoteTo;
import ru.dosov.restvoting.util.AuthUser;
import ru.dosov.restvoting.util.DateTimeUtil;
import ru.dosov.restvoting.util.VoteUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.dosov.restvoting.util.ValidationUtil.*;
import static ru.dosov.restvoting.util.VoteUtil.getListTo;
import static ru.dosov.restvoting.util.VoteUtil.getTo;

@RestController
@RequestMapping(value = "/api/v1/votes")
public class VoteController {

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public VoteController(VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteTo create(@Valid @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        checkNew(voteTo);
        checkPermission(authUser, voteTo.getUser_id());
        LocalDateTime voteDateTime = DateTimeUtil.fillVoteDate(voteTo.getDateTime());
        Vote vote = new Vote(
                null,
                voteDateTime,
                authUser.getUser(),
                restaurantRepository.getOne(voteTo.getRestaurant_id())
        );
        Vote savedVote = voteRepository.save(vote);
        return getTo(savedVote);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping
    public List<VoteTo> getAllByUserAndDate(
            @RequestParam @Nullable Integer user,
            @RequestParam @Nullable LocalDate start,
            @RequestParam @Nullable LocalDate end
    ) {
        LocalDateTime startDay = DateTimeUtil.getDateTimeOrMin(start);
        LocalDateTime endDay = DateTimeUtil.getDateTimeOrMax(end);
        List<Vote> votes = user == null
                ? voteRepository.getAllByDate(startDay, endDay)
                : voteRepository.getAllByUserOrDate(user, startDay, endDay);
        return getListTo(votes);
    }


    @GetMapping(value = "/{id}")
    public VoteTo getVoteById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser authUser) {
        Vote vote = checkNotFound(voteRepository.findById(id).orElse(null), id);
        checkPermission(authUser, vote.getUser().getId());
        return VoteUtil.getTo(vote);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @Valid @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, voteTo.getUser_id());
        LocalDateTime voteDateTime = DateTimeUtil.fillVoteDate(voteTo.getDateTime());
        checkVoteTime(voteDateTime, AppConfig.DEAD_LINE);
        assureIdConsistent(voteTo, id);
        Vote vote = new Vote(
                id,
                voteDateTime,
                authUser.getUser(),
                restaurantRepository.getOne(voteTo.getRestaurant_id())
        );
        voteRepository.save(vote);
    }

    @Transactional
    @DeleteMapping(value = "/{vote_id}/")
    public void delete(@PathVariable Integer vote_id, @RequestParam("user") Integer user_id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, user_id);
        Vote voteToDelete = checkNotFound(voteRepository.findById(vote_id).orElse(null), vote_id);
        checkVoteTime(voteToDelete.getDateTime(), AppConfig.DEAD_LINE);
        voteRepository.delete(vote_id);
    }
}
