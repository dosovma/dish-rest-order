package ru.dosov.restvoting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.validator.constraints.Length;
import ru.dosov.restvoting.model.AbstractEntity.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "Restaurant")
public class Restaurant extends NamedEntity {

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Menu> menus;

    public Restaurant(Integer id, @NotNull @Length(min = 2, max = 256) String name, Set<Menu> menus) {
        super(id, name);
        this.menus = menus;
    }

    public Restaurant() {
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menus=" + menus +
                '}';
    }
}
