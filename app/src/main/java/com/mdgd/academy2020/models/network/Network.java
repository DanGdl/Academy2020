package com.mdgd.academy2020.models.network;

import com.mdgd.academy2020.models.schemas.AvatarUpdate;
import com.mdgd.academy2020.models.schemas.UserData;

import io.reactivex.Single;

public interface Network {
    boolean hasUser();

    Single<Result<String>> loginUser(String email, String password);

    Single<Result<String>> loginNewUser(String email, String password);

    void logOut();

    Single<Result<AvatarUpdate>> uploadImage(String fileName);

    Single<Result<Boolean>> updateUser(UserData data);

    Single<Result<UserData>> getUser();
}
