package ru.dosov.restvoting.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dosov.restvoting.model.AbstractEntity.HasId;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.util.Objects;

public class VoteTo implements HasId<Integer> {
    private Integer id;
    @NotNull
    private Integer restaurant_id;

    @ConstructorProperties({"id", "restaurant_id"})
    public VoteTo(Integer id, Integer restaurant_id) {
        this.id = id;
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
        return Objects.equals(id, voteTo.id) && Objects.equals(restaurant_id, voteTo.restaurant_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurant_id);
    }
}
