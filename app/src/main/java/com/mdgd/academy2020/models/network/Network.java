package com.mdgd.academy2020.models.network;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface Network {
    boolean hasUser();

    Single<Result<String>> execLogin(String email, String password);

    Single<Result<String>> createNewUser(String nickname, String email, String password, String imageUrl);

    void logOut();

    Completable uploadImage(File file, String fileName);
}
