package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.Dish;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {
}
