package com.mdgd.academy2020.models.repo.avatar;

import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.schemas.AvatarUpdate;

import java.util.List;

import io.reactivex.Single;

public interface AvatarRepository {

    void onConfigurationChanged();

    Single<String> getUrl();

    Single<String> generateNewUrl();

    Single<Result<String>> captureImage(Single<Result<String>> resultSingle);

    Single<Result<AvatarUpdate>> uploadAvatar(String imageUrl);

    String downloadAvatar(String imageUrl);

    Single<String> generateNewUrl(String type);

    List<String> getTypes();

    String getType();

    void removeFiles();

    Single<Result<Boolean>> delete(String imageUrl);
}
