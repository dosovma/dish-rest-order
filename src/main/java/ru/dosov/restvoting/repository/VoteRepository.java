package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.Vote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:id and v.voteDate<=:dateEnd and v.voteDate>=:dateStart ORDER BY v.voteDate DESC")
    List<Vote> getAllByUserOrDate(@Param("id") int id, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Query("SELECT v FROM Vote v WHERE v.voteDate<=:dateEnd and v.voteDate>=:dateStart ORDER BY v.voteDate DESC")
    List<Vote> getAllByDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id and v.id=:vote_id")
    Optional<Vote> getVoteByIdAndUser(@Param("user_id") Integer user_id, @Param("vote_id") Integer vote_id);
}