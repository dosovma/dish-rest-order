package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.dosov.restvoting.Util.VoteUtil;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.repository.VoteRepository;
import ru.dosov.restvoting.to.VoteTo;

import java.time.LocalDateTime;
import java.util.List;

import static ru.dosov.restvoting.Util.ValidationUtil.*;

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

    @GetMapping
    public List<VoteTo> getAll() {
        List<Vote> votes = voteRepository.findAll();
        return VoteUtil.getListTo(votes);
    }

    //TODO not allowed do on METHOD permission чтобы только админ мог этот метод вызывать
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteTo create(@RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        checkNew(voteTo);
        checkPermission(authUser, voteTo.getUser_id());
        Vote vote = new Vote(
                null,
                LocalDateTime.now(),
                authUser.getUser(),
                restaurantRepository.getOne(voteTo.getRestaurant_id())
        );
        Vote savedVote = voteRepository.save(vote);
        return VoteUtil.getTo(savedVote);
    }

    @GetMapping(value = "/{id}")
    public VoteTo getVoteById(@PathVariable Integer id) {
        Vote vote = voteRepository.findById(id).get();
        return VoteUtil.getTo(vote);
    }

    //TODO do check TIME to update vote
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable Integer id, @RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, voteTo.getUser_id());
        assureIdConsistent(voteTo, id);
        Vote vote = new Vote(
                id,
                LocalDateTime.now(),
                authUser.getUser(),
                restaurantRepository.getOne(voteTo.getRestaurant_id())
        );
        voteRepository.save(vote);
    }

    //TODO do check time to delete vote
    @DeleteMapping(value = "/{vote_id}")
    public void delete(@PathVariable Integer vote_id, @RequestParam("user") Integer user_id, @AuthenticationPrincipal AuthUser authUser) {
        checkPermission(authUser, user_id);
        checkNotFound(voteRepository.delete(vote_id) != 0, vote_id);
    }
}
