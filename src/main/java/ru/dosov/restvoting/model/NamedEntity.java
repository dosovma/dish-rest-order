package ru.dosov.restvoting.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {
    @NotNull
    @Length(min = 2, max = 256)
    protected String name;

    public NamedEntity(Integer id, @NotNull @Length(min = 2, max = 256) String name) {
        super(id);
        this.name = name;
    }

    public NamedEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
