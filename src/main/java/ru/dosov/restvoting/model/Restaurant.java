package ru.dosov.restvoting.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;
import ru.dosov.restvoting.model.AbstractEntity.NamedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "Restaurant")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Restaurant extends NamedEntity {

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    private Set<Menu> menus;

    public Restaurant(Integer id, @NotNull @Length(min = 2, max = 256) String name, Set<Menu> menus) {
        super(id, name, true);
        this.menus = menus;
    }

    public Restaurant(Integer id, @NotNull @Length(min = 2, max = 256) String name) {
        super(id, name, true);
        this.menus = null;
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
                '}';
    }
}
