package com.mdgd.academy2020.models.dao;

import java.util.List;

public interface Dao<T extends Entity> {

    void open();

    void close();

    void save(T entity);

    void save(List<T> entities);

    T get(long id);

    List<T> get(List<Long> ids);

    void delete(long id);

    void deleteById(List<Long> ids);

    void delete(T entity);

    void delete(List<T> entities);

    void deleteAll();
}
