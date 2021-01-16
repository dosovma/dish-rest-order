package ru.dosov.restvoting.model;

import org.springframework.data.domain.Persistable;

public interface HasId<ID> extends Persistable<ID> {
    void setId(ID id);
}