package ru.dosov.restvoting.model.AbstractEntity;

import org.springframework.data.domain.Persistable;

public interface HasId<ID> extends Persistable<ID> {
    void setId(ID id);
}