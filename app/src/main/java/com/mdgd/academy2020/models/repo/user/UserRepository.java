package com.mdgd.academy2020.models.repo.user;

import com.mdgd.academy2020.models.network.Result;

import io.reactivex.Single;

public interface UserRepository {
    Single<Result<User>> createNewUser(String uid, String email, String imageUrl, String nickname);

    Single<Result<User>> getUser(String uid);

    void clear();
}
