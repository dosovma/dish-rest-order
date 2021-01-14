package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dosov.restvoting.model.Restaurant;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.repository.VoteRepository;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/v1")
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

    @GetMapping(value = "/users/{user_id}/votes/{rest_id}")
    public Vote createVote(@PathVariable Integer user_id, @PathVariable Integer rest_id, @AuthenticationPrincipal AuthUser authUser) {
        Restaurant restaurant = restaurantRepository.getById(rest_id);
        Vote vote = new Vote(null, LocalDateTime.now(), authUser.getUser(), restaurant);
        return voteRepository.save(vote);
    }

    @GetMapping(value = "/restaurants/{id}")
    public Restaurant getRestaurantById(@PathVariable Integer id) {
        return restaurantRepository.getById(id);
    }
}
