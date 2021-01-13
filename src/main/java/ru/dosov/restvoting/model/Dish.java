package ru.dosov.restvoting.model;

import ru.dosov.restvoting.model.AbstractEntity.NamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "dish")
public class Dish extends NamedEntity {

    @Column(name = "price")
    private Double price;

    @ManyToMany(mappedBy = "dishes")
    private Set<Menu> menus;

    public Dish(int id, String name, Double price, Set<Menu> menus) {
        super(id, name);
        this.price = price;
        this.menus = menus;
    }

    public Dish() {
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menus=" + menus +
                '}';
    }
}
