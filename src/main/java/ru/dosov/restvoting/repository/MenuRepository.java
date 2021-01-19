package ru.dosov.restvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.dosov.restvoting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Modifying
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:id and m.date>=:dateStart and m.date<=:dateEnd ORDER BY m.date DESC")
    List<Menu> getMenuByRestaurantBetweenDate(@Param("id") Integer id, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Modifying
    @Query("SELECT m FROM Menu m WHERE m.date>=:dateStart and m.date<=:dateEnd ORDER BY m.date DESC")
    List<Menu> getMenuBetweenDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Modifying
    @Query("SELECT m FROM Menu m WHERE m.date=:date ORDER BY m.date DESC")
    List<Menu> getMenuByDate(@Param("date") LocalDate date);

    @Modifying
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:id and m.date=:date ORDER BY m.date DESC")
    List<Menu> getMenuByRestaurantAndDate(@Param("id") Integer id, @Param("date") LocalDate date);

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);
}