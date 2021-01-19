package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.Vote;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

    @Modifying
    @Query("SELECT v FROM Vote v WHERE v.user.id=:id and v.dateTime <=: dateEnd and v.dateTime >=: dateStart")
    List<Vote> getAllByUserOrDate(@Param("id") int id, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd);

    @Modifying
    @Query("SELECT v FROM Vote v WHERE v.dateTime <=: dateEnd and v.dateTime >=: dateStart")
    List<Vote> getAllByDate(@Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd);
}