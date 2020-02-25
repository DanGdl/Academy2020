package com.mdgd.academy2020.models;

import android.app.Application;

import com.mdgd.academy2020.models.cache.Cache;
import com.mdgd.academy2020.models.cache.CacheImpl;

public class DefaultModelProvider implements ModelProvider {
    private final Application app;
    private final Cache cache;

    public DefaultModelProvider(Application app) {
        this.app = app;
        cache = new CacheImpl();
    }

    @Override
    public Cache getCache() {
        return cache;
    }
}
