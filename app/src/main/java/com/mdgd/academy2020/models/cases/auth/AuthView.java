package com.mdgd.academy2020.models.cases.auth;

public interface AuthView {
    boolean isStarted();

    void showProgress();

    void hideProgress();

    String getString(int strResId, Object... args);

    void showError(String title, String message);

    void showToast(int strResId, Object... args);

    void proceedToLobby();
}
