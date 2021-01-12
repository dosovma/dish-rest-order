package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.User;

import java.util.List;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

/*    @Query("SELECT u FROM User u")
    List<User> getAll();*/
}
