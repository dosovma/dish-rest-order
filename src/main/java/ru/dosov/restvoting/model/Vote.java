package ru.dosov.restvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ru.dosov.restvoting.model.AbstractEntity.DatedEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = {"rest_id", "user_id", "date"}, name = "restaurant_user_date_unique_idx"))
public class Vote extends DatedEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    @JsonBackReference
    private Restaurant restaurant;

    public Vote(Integer id, LocalDateTime date, User user, Restaurant restaurant) {
        super(id, date);
        this.user = user;
        this.restaurant = restaurant;
    }

    public Vote() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", user=" + user +
                ", restaurant=" + restaurant +
                '}';
    }
}
