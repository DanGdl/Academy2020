package com.mdgd.academy2020.models.avatar;

import com.mdgd.academy2020.dto.AvatarUpdate;
import com.mdgd.academy2020.models.network.Result;

import io.reactivex.Single;

public interface AvatarRepository {
    Single<String> getUrl();

    Single<String> generateNewUrl();

    Single<Result<String>> captureImage(Single<Result<String>> resultSingle);

    Single<Result<AvatarUpdate>> uploadAvatar(String imageUrl);

    String downloadAvatar(String imageUrl);

    void putAvatarUrl(String avatarUrl);

    void putAvatarPath(String avatarPath);

    void clear();
}
