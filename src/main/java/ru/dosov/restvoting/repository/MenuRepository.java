package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.dishes WHERE m.restaurant.id=:id and m.date>=:dateStart and m.date<=:dateEnd ORDER BY m.date DESC")
    List<Menu> getAllByRestaurantBetweenDate(@Param("id") Integer id, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.dishes WHERE m.date>=:dateStart and m.date<=:dateEnd ORDER BY m.date DESC")
    List<Menu> getAllBetweenDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.dishes WHERE m.date=:date ORDER BY m.date DESC")
    List<Menu> getAllByDate(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.dishes WHERE m.restaurant.id=:id and m.date=:date ORDER BY m.date DESC")
    List<Menu> getAllByRestaurantAndDate(@Param("id") Integer id, @Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.dishes WHERE m.id=:id")
    Optional<Menu> getOneById(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);
}