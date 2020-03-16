package com.mdgd.academy2020.models.network;

import com.mdgd.academy2020.dto.LoginResponse;

import io.reactivex.Single;

public interface Network {
    boolean hasUser();

    Single<Result<LoginResponse>> execLogin(String email, String password);

    Single<Result<LoginResponse>> createNewUser(String nickname, String email, String password, String imageUrl);

    void logOut();

    Single<Result<String>> uploadImage(String fileName);
}
