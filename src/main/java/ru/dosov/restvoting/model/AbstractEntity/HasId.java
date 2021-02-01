package ru.dosov.restvoting.model.AbstractEntity;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;

public interface HasId<ID> extends Persistable<ID>, Serializable {
    void setId(ID id);
}