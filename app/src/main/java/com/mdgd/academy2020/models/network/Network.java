package com.mdgd.academy2020.models.network;

import com.mdgd.academy2020.dto.AvatarUpdate;
import com.mdgd.academy2020.dto.UserData;

import io.reactivex.Single;

public interface Network {
    boolean hasUser();

    Single<Result<String>> execLogin(String email, String password);

    Single<Result<String>> createNewUser(String email, String password);

    void logOut();

    Single<Result<AvatarUpdate>> uploadImage(String fileName);

    Single<Result<Boolean>> updateUser(UserData data);

    Single<Result<UserData>> getUser();
}
