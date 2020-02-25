package com.mdgd.academy2020.models.network;

public interface Callback<T> {
    void onResult(Result<T> result);
}
