package ru.dosov.restvoting;

import java.time.LocalDateTime;

public class VoteTo {
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

    public Integer getId() {
        return id;
    }

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
}
