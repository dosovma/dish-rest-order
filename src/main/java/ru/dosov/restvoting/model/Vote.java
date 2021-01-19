package ru.dosov.restvoting.model;

import ru.dosov.restvoting.model.AbstractEntity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
public class Vote extends BaseEntity {

    @NotNull
    @Column(name = "date")
    private LocalDateTime dateTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rest_id")
    private Restaurant restaurant;

    public Vote(Integer id, LocalDateTime date, User user, Restaurant restaurant) {
        super(id);
        this.dateTime = date;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Vote() {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", user=" + user +
                ", restaurant=" + restaurant +
                '}';
    }
}
