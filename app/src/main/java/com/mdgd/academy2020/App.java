package com.mdgd.academy2020;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.mdgd.academy2020.models.DefaultModelProvider;
import com.mdgd.academy2020.models.ModelProvider;

public class App extends Application {
    private static App instance;
    private ModelProvider modelProvider;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseApp.initializeApp(this);
        modelProvider = new DefaultModelProvider(this);
    }

    public ModelProvider getModelProvider() {
        return modelProvider;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        modelProvider.onConfigurationChanged();
    }
}
