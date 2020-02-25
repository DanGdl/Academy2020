package com.mdgd.academy2020.models;

import android.app.Application;

public class DefaultModelProvider implements ModelProvider {
    private final Application app;

    public DefaultModelProvider(Application app) {
        this.app = app;
    }
}
