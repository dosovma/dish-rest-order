package ru.dosov.restvoting.Util;

import ru.dosov.restvoting.VoteTo;
import ru.dosov.restvoting.model.Vote;

import java.util.List;
import java.util.stream.Collectors;

public class VoteUtil {
    private VoteUtil() {
    }

    public static VoteTo getTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDateTime(), vote.getUser().getId(), vote.getRestaurant().getId());
    }

    public static List<VoteTo> getListTo(List<Vote> votes) {
        return votes.stream().map(VoteUtil::getTo).collect(Collectors.toList());
    }
}