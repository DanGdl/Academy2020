package com.mdgd.academy2020.models.network;

import io.reactivex.Notification;
import io.reactivex.Single;

public interface Network {
    boolean hasUser();

    Single<Result<Notification>> execLogin(String email, String password);
}
