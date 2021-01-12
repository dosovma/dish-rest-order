package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.Menu;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
