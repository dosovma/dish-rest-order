package ru.dosov.restvoting.model;

import ru.dosov.restvoting.model.AbstractEntity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote")
public class Vote extends BaseEntity {

    @NotNull
    @Column(name = "vote_date")
    private LocalDate voteDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rest_id")
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate voteDate, User user, Restaurant restaurant) {
        super(id, true);
        this.voteDate = voteDate;
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

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", dateTime=" + voteDate +
                ", user=" + user +
                ", restaurant=" + restaurant +
                '}';
    }
}
