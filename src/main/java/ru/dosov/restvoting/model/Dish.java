package ru.dosov.restvoting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ru.dosov.restvoting.model.AbstractEntity.NamedEntity;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Set;

@Entity
@Table(name = "dish")
public class Dish extends NamedEntity {

    @Positive
    @Column(name = "price")
    private Long price;

    @ManyToMany(mappedBy = "dishes", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Menu> menus;

    public Dish(int id, String name, Long price, Set<Menu> menus, Boolean enable) {
        super(id, name, enable);
        this.price = price;
        this.menus = menus;
    }

    public Dish(int id, String name, Long price, Boolean enable) {
        super(id, name, enable);
        this.price = price;
        this.menus = null;
    }

    public Dish() {
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public void setEnable(Boolean enable) {
        this.enabled = enable;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", enable=" + enabled +
                ", name='" + name +
                ", price=" + price +
                '}';
    }
}
