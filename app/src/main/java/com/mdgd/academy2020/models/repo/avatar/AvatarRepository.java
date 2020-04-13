package com.mdgd.academy2020.models.repo.avatar;

import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.schemas.AvatarUpdate;

import io.reactivex.Single;

public interface AvatarRepository {
    Single<String> getUrl();

    Single<String> generateNewUrl();

    Single<Result<String>> captureImage(Single<Result<String>> resultSingle);

    Single<Result<AvatarUpdate>> uploadAvatar(String imageUrl);

    String downloadAvatar(String imageUrl);

    void setType(String type);
}
