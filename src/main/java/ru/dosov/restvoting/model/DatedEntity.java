package ru.dosov.restvoting.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class DatedEntity extends BaseEntity {

    @Column(name = "date")
    private LocalDateTime date;

    protected DatedEntity(Integer id, LocalDateTime date) {
        super(id);
        this.date = date;
    }

    protected DatedEntity() {
        super();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
