package ru.dosov.restvoting.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

@MappedSuperclass
public abstract class DatedEntity extends BaseEntity {

    @Column(name = "date")
    private LocalDate date;

    protected DatedEntity(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }

    protected DatedEntity() {
        super();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
