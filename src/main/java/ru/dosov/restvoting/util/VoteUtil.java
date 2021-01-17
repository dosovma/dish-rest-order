package ru.dosov.restvoting.util;

import ru.dosov.restvoting.model.Vote;
import ru.dosov.restvoting.to.VoteTo;
import ru.dosov.restvoting.util.exception.ForbiddenException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static void checkVoteTime(LocalDateTime voteDateTime, LocalTime deadLine) {
        if (voteDateTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new ForbiddenException("Unfortunately you can't change or delete your old vote");
        }
        if (voteDateTime.toLocalTime().isAfter(deadLine)) {
            throw new ForbiddenException("Unfortunately you can't change or delete your vote after " + deadLine);
        }
    }
}