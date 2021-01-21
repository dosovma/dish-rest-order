package ru.dosov.restvoting.util;

import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.to.VoteTo;

import java.util.List;
import java.util.stream.Collectors;

public class VoteUtil {
    private VoteUtil() {
    }

    public static VoteTo getTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDateTime(), vote.getUser().getId(), vote.getRestaurant().getId());
    }

    public static List<VoteTo> getListTo(List<Vote> votes) {
        return votes == null
                ? List.of()
                : votes.stream().map(VoteUtil::getTo).collect(Collectors.toList());
    }
}