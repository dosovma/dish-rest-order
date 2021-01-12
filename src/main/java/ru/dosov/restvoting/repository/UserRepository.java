package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.User;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

}
