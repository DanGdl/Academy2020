package com.mdgd.academy2020.models.prefs;

public interface Prefs {
    String getAuthToken();

    void putAuthToken(String token);
}