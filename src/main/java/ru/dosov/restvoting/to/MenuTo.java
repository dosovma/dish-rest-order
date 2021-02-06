package ru.dosov.restvoting.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dosov.restvoting.model.AbstractEntity.HasId;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class MenuTo implements HasId<Integer> {
    private Integer id;
    @NotNull
    private LocalDate date;
    @NotNull
    private Integer restaurant_id;
    private Set<Integer> dishes;

    public MenuTo(Integer id, LocalDate date, Integer restaurant_id, Set<Integer> dishes) {
        this.id = id;
        this.date = date;
        this.restaurant_id = restaurant_id;
        this.dishes = dishes;
    }

    public MenuTo() {
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Set<Integer> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Integer> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuTo)) return false;
        MenuTo menuTo = (MenuTo) o;
        return Objects.equals(id, menuTo.id) && Objects.equals(date, menuTo.date) && Objects.equals(restaurant_id, menuTo.restaurant_id) && Objects.equals(dishes, menuTo.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, restaurant_id, dishes);
    }
}