package com.mdgd.academy2020.models.dao;

import com.google.common.collect.FluentIterable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SqlDao<T extends Entity> implements Dao<T> {

    @Override
    public void save(T entity) {
        if (entity != null) {
            save(Collections.singletonList(entity));
        }
    }

    @Override
    public void save(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        // todo impl
    }

    @Override
    public T get(long id) {
        final List<T> ts = get(Collections.singletonList(id));
        return ts.isEmpty() ? null : ts.get(0);
    }

    @Override
    public List<T> get(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        // todo impl
        return new ArrayList<>();
    }

    @Override
    public void delete(long id) {
        deleteById(Collections.singletonList(id));
    }

    @Override
    public void deleteById(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            return;
        }
        // todo impl
    }

    @Override
    public void delete(T entity) {
        if (entity != null) {
            deleteById(Collections.singletonList(entity.getId()));
        }
    }

    @Override
    public void delete(List<T> entities) {
        if (entities != null && !entities.isEmpty()) {
            deleteById(FluentIterable.from(entities)
                    .filter(e -> e != null)
                    .transform(e -> e.getId())
                    .toList());
        }
    }

    @Override
    public void deleteAll() {

    }
}
