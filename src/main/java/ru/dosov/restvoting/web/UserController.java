package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dosov.restvoting.Util.VoteUtil;
import ru.dosov.restvoting.VoteTo;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.repository.VoteRepository;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public UserController(UserRepository userRepository, VoteRepository voteRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping(value = "/{user_id}/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteTo createVote(@RequestBody VoteTo voteTo, @AuthenticationPrincipal AuthUser authUser) {
        Integer restaurant_id = voteTo.getRestaurant_id();
        Vote vote = new Vote(null, LocalDateTime.now(), authUser.getUser(), restaurantRepository.getOne(restaurant_id));
        Vote savedVote = voteRepository.save(vote);
        return VoteUtil.getTo(savedVote);
    }
}
