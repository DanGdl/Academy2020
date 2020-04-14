package com.mdgd.academy2020.models.dao;

public class Entity {
    public static final long DEFAULT_ID = -1L;

    private long id = DEFAULT_ID;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
