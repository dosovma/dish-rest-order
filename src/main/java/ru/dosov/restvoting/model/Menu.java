package ru.dosov.restvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ru.dosov.restvoting.model.AbstractEntity.DatedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "menu", uniqueConstraints = @UniqueConstraint(columnNames = {"rest_id", "date"}, name = "restaurant_menu_unique_date_idx"))
public class Menu extends DatedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "dish_menu",
            joinColumns = @JoinColumn(name = "menu_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"dish_id", "menu_id"}, name = "dish_menu_unique")
    )
    @JsonManagedReference
    private Set<Dish> dishes;

    public Menu(Integer id, LocalDateTime date, Restaurant restaurant, Set<Dish> dishes) {
        super(id, date);
        this.restaurant = restaurant;
        this.dishes = dishes;
    }

    public Menu() {
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Set<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(Set<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", dishes=" + dishes +
                '}';
    }
}
