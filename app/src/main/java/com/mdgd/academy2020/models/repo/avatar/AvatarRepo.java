package com.mdgd.academy2020.models.repo.avatar;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.files.Files;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.res.AndroidResources;
import com.mdgd.academy2020.models.schemas.AvatarUpdate;
import com.mdgd.academy2020.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AvatarRepo implements AvatarRepository {
    private final Map<String, String> types = new HashMap<>();
    private final ProfileCache profileCache;
    private final AndroidResources res;
    private final Network network;
    private final Prefs prefs;
    private final Files files;

    public AvatarRepo(ProfileCache profileCache, Prefs prefs, Files files, Network network, AndroidResources res) {
        this.profileCache = profileCache;
        this.network = network;
        this.prefs = prefs;
        this.files = files;
        this.res = res;

        onConfigurationChanged();
    }

    @Override
    public void onConfigurationChanged() {
        types.put(res.getString(R.string.avatar_type_abstract), "identicon");
        types.put(res.getString(R.string.avatar_type_monster), "monsterid");
        types.put(res.getString(R.string.avatar_type_robot), "robohash");
    }

    @Override
    public Single<String> getUrl() {
        return Single.just(profileCache.getImageUrl())
                .flatMap(url -> TextUtil.isEmpty(url) ? checkAvatarHash() : Single.just(url));
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
                }).map(hash -> generate(hash, profileCache.getAvatarType()))
                .doOnEvent((url, error) -> profileCache.putImageUrl(url));
    }

    @Override
    public Single<String> generateNewUrl() {
        return generateHash().map(hash -> generate(hash, profileCache.getAvatarType()))
                .doOnEvent((url, error) -> profileCache.putImageUrl(url));
    }

    private Single<String> generateHash() {
        return Single.just(new Random().nextInt((int) 10E6))
                .map(String::valueOf)
                .flatMap(hash -> Completable.fromAction(() -> {
                    profileCache.putAvatarHash(hash);
                    prefs.putImageHash(hash);
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .andThen(Single.just(hash)));
    }

    private String generate(String hash, String type) {
        // https://www.gravatar.com/avatar/5148955656?d=identicon&r=g
        // d: identicon, monsterid, robohash
        // r: g, pg, r, x
        return String.format("https://www.gravatar.com/avatar/%1$s?d=" + type + "&r=x&s=512", hash);
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

    @Override
    public Single<String> generateNewUrl(String type) {
        if (profileCache.getAvatarType().equals(type) || TextUtil.isEmpty(type)) {
            return Single.never();
        }
        if (!TextUtil.isEmpty(types.get(type))) {
            profileCache.putAvatarType(type);
            return Single.just(profileCache.getAvatarHash())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(hash -> generate(hash, type))
                    .doOnEvent((url, error) -> profileCache.putImageUrl(url));
        } else {
            return Single.never();
        }
    }

    @Override
    public List<String> getTypes() {
        return new ArrayList<>(types.keySet());
    }
}
