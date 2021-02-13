package ru.dosov.restvoting.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dosov.restvoting.model.AbstractEntity.HasId;

import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;
import java.util.Objects;

public class VoteTo implements HasId<Integer> {
    private Integer id;
    @NotNull
    private Integer restaurantId;

    @ConstructorProperties({"id", "restaurantId"})
    public VoteTo(Integer id, Integer restaurantId) {
        this.id = id;
        this.restaurantId = restaurantId;
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

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VoteTo)) return false;
        VoteTo voteTo = (VoteTo) o;
        return Objects.equals(id, voteTo.id) && Objects.equals(restaurantId, voteTo.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, restaurantId);
    }
}
