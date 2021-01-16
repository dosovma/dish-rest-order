package ru.dosov.restvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;
import ru.dosov.restvoting.model.AbstractEntity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"}, name = "restaurant_user_date_unique_idx"))
public class Vote extends BaseEntity {

    @Value("${appattributes.deadline}")
    @JsonIgnore
    private LocalTime deadLine;

    @Column(name = "date")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonBackReference
    private User user;

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

    public LocalTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalTime deadLine) {
        this.deadLine = deadLine;
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
                ", user=" + user +
                ", restaurant=" + restaurant +
                '}';
    }
}
