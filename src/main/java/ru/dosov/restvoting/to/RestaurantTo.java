package ru.dosov.restvoting.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.dosov.restvoting.model.AbstractEntity.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class RestaurantTo implements HasId<Integer> {
    private Integer id;
    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    public RestaurantTo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public RestaurantTo() {
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantTo)) return false;
        RestaurantTo that = (RestaurantTo) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}