package com.mdgd.academy2020.models.repo.avatar;

import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.files.Files;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.repo.avatar.generator.AvatarUrlGenerator;
import com.mdgd.academy2020.models.schemas.AvatarUpdate;
import com.mdgd.academy2020.util.TextUtil;

import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AvatarRepo implements AvatarRepository {
    private final AvatarUrlGenerator avatarUrlGenerator;
    private final ProfileCache profileCache;
    private final Network network;
    private final Prefs prefs;
    private final Files files;

    public AvatarRepo(ProfileCache profileCache, AvatarUrlGenerator avatarUrlGenerator, Prefs prefs, Files files, Network network) {
        this.avatarUrlGenerator = avatarUrlGenerator;
        this.profileCache = profileCache;
        this.network = network;
        this.prefs = prefs;
        this.files = files;
    }

    @Override
    public Single<String> getUrl() {
        return Single.just(profileCache.getImageUrl())
                .flatMap(url -> TextUtil.isEmpty(url) ? checkAvatarHash() : Single.just(url));
    }

    @Override
    public Single<String> generateNewUrl() {
        return generateHash().map(avatarUrlGenerator::generate)
                .doOnEvent((url, error) -> profileCache.putImageUrl(url));
    }

    @Override
    public Single<Result<String>> captureImage(Single<Result<String>> resultSingle) {
        return resultSingle
                .doOnEvent((result, error) -> {
                    if (!result.isFail()) {
                        profileCache.putImageUrl(result.data);
                    }
                });
    }

    private Single<String> checkAvatarHash() {
        return Single.just(prefs.getAvatarHash())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(hash -> {
                    if (TextUtil.isEmpty(hash)) {
                        return generateHash();
                    }
                    return Single.just(hash);
                }).map(avatarUrlGenerator::generate)
                .doOnEvent((url, error) -> profileCache.putImageUrl(url));
    }

    private Single<String> generateHash() {
        return Single.just(new Random().nextInt((int) 10E6))
                .map(String::valueOf)
                .flatMap(hash -> Completable.fromAction(() -> prefs.putImageHash(hash))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .andThen(Single.just(hash)));
    }


    @Override
    public Single<Result<AvatarUpdate>> uploadAvatar(String imageUrl) {
        final String imagePath;
        if (imageUrl.contains("http")) {
            imagePath = downloadAvatar(imageUrl);
        } else {
            final Result<String> result = files.getCopyFromPath(imageUrl);
            if (result.isFail()) {
                return Single.just(new Result<>(result.error));
            }
            imagePath = result.data;
        }
        return network.uploadImage(imagePath);
    }

    @Override
    public String downloadAvatar(String imageUrl) {
        return files.downloadFile(imageUrl);
    }
}
