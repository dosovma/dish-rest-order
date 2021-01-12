package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.Vote;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
}