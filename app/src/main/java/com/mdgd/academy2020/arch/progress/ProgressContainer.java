package com.mdgd.academy2020.arch.progress;

public interface ProgressContainer {

    boolean hasProgress();

    void showProgress(String title, String message);

    void showProgress(int titleRes, int messageRes);

    void showProgress();

    void hideProgress();
}
