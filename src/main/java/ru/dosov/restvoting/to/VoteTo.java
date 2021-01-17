package ru.dosov.restvoting.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dosov.restvoting.model.HasId;

import java.time.LocalDateTime;
import java.util.Objects;

public class VoteTo implements HasId<Integer> {
    private Integer id;
    private LocalDateTime dateTime;
    private Integer user_id;
    private Integer restaurant_id;

    public VoteTo(Integer id, LocalDateTime dateTime, Integer user_id, Integer restaurant_id) {
        this.id = id;
        this.dateTime = dateTime;
        this.user_id = user_id;
        this.restaurant_id = restaurant_id;
    }

    public VoteTo() {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(Integer restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteTo)) return false;
        VoteTo voteTo = (VoteTo) o;
        return Objects.equals(id, voteTo.id) && Objects.equals(dateTime, voteTo.dateTime)
                && Objects.equals(user_id, voteTo.user_id)
                && Objects.equals(restaurant_id, voteTo.restaurant_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, user_id, restaurant_id);
    }
}
