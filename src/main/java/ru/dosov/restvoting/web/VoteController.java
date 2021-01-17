package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.Util.VoteUtil;
import ru.dosov.restvoting.config.AppConfig;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.repository.VoteRepository;
import ru.dosov.restvoting.to.VoteTo;

import java.util.List;

import static ru.dosov.restvoting.Util.ValidationUtil.*;
import static ru.dosov.restvoting.Util.VoteUtil.*;

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

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping
    public List<VoteTo> getAll() {
        List<Vote> votes = voteRepository.findAll();
        return getListTo(votes);
    }

    @GetMapping
    public List<VoteTo> getAllByUser(@RequestParam("user") Integer user_id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, user_id);
        List<Vote> votes = voteRepository.getAllByUserId(user_id);
        return getListTo(votes);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteTo create(@RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        checkNew(voteTo);
        checkPermission(authUser, voteTo.getUser_id());
        Vote vote = new Vote(
                null,
                voteTo.getDateTime(),
                authUser.getUser(),
                restaurantRepository.getOne(voteTo.getRestaurant_id())
        );
        Vote savedVote = voteRepository.save(vote);
        return getTo(savedVote);
    }


    //TODO work with optional findById(id)
    @GetMapping(value = "/{id}")
    public VoteTo getVoteById(@PathVariable Integer id, @AuthenticationPrincipal AuthUser authUser) {
        Vote vote = voteRepository.findById(id).get();
        checkPermission(authUser, vote.getUser().getId());
        return VoteUtil.getTo(vote);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, voteTo.getUser_id());
        checkVoteTime(voteTo.getDateTime(), AppConfig.DEAD_LINE);
        assureIdConsistent(voteTo, id);
        Vote vote = new Vote(
                id,
                voteTo.getDateTime(),
                authUser.getUser(),
                restaurantRepository.getOne(voteTo.getRestaurant_id())
        );
        voteRepository.save(vote);
    }

    @DeleteMapping(value = "/{vote_id}/")
    public void delete(@PathVariable Integer vote_id, @RequestParam("user") Integer user_id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, user_id);
        Vote voteToDelete = checkNotFound(voteRepository.findById(vote_id).orElse(null), vote_id);
        checkVoteTime(voteToDelete.getDateTime(), AppConfig.DEAD_LINE);
        voteRepository.delete(vote_id);
    }
}
