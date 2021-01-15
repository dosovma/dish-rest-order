package ru.dosov.restvoting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dosov.restvoting.Util.VoteUtil;
import ru.dosov.restvoting.VoteTo;
import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.repository.RestaurantRepository;
import ru.dosov.restvoting.repository.UserRepository;
import ru.dosov.restvoting.repository.VoteRepository;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/votes")
public class VoteController {

    private final VoteRepository voteRepository;

    @Autowired
    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public List<VoteTo> getVotes() {
        List<Vote> votes = voteRepository.findAll();
        return VoteUtil.getListTo(votes);
    }

    @GetMapping(value = "/{id}")
    public VoteTo getVoteById(@PathVariable("id") Integer id) {
        Vote vote = voteRepository.findById(id).get();
        return VoteUtil.getTo(vote);
    }

}
