package ru.dosov.restvoting.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "Restaurant")
public class Restaurant extends NamedEntity {

    @OneToMany(mappedBy = "restaurant")
    private Set<Menu> menus;

    @OneToMany(mappedBy = "restaurant")
    private Set<Vote> votes;

    public Restaurant(Integer id, @NotNull @Length(min = 2, max = 256) String name, Set<Menu> menus, Set<Vote> votes) {
        super(id, name);
        this.menus = menus;
        this.votes = votes;
    }

    public Restaurant() {
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menus=" + menus +
                ", votes=" + votes +
                '}';
    }
}
