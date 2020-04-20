package com.mdgd.academy2020.models.repo.avatar;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
import com.mdgd.academy2020.models.files.Files;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.res.AndroidResources;
import com.mdgd.academy2020.models.schemas.AvatarUpdate;
import com.mdgd.academy2020.util.Pair;
import com.mdgd.academy2020.util.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.Notification;
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
        return Single.fromCallable(() -> new Pair<>(prefs.getAvatarHash(), prefs.getAvatarType()))
                .subscribeOn(Schedulers.io())
                .doOnEvent((pair, error) -> profileCache.putAvatarType(pair.second))
                .flatMap(pair -> {
                    if (TextUtil.isEmpty(pair.first)) {
                        return generateHash().map(hash -> new Pair<>(hash, pair.second));
                    }
                    return Single.just(pair);
                }).map(pair -> generate(pair.first, profileCache.getAvatarType()))
                .doOnEvent((url, error) -> profileCache.putImageUrl(url))
                .observeOn(AndroidSchedulers.mainThread());
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
        return String.format("https://www.gravatar.com/avatar/%1$s?d=%2$s&r=x&s=512", hash, type);
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
            profileCache.putAvatarType(types.get(type));
            return Single.fromCallable(() -> {
                prefs.putAvatarType(types.get(type));
                return Notification.createOnNext(new Object());
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(event -> generate(profileCache.getAvatarHash(), profileCache.getAvatarType()))
                    .doOnEvent((url, error) -> profileCache.putImageUrl(url));
        } else {
            return Single.never();
        }
    }

    @Override
    public List<String> getTypes() {
        return new ArrayList<>(types.keySet());
    }

    @Override
    public String getType() {
        for (Map.Entry<String, String> e : types.entrySet()) {
            if (profileCache.getAvatarType().equals(e.getValue())) {
                return e.getKey();
            }
        }
        return "";
    }

    @Override
    public void removeFiles() {
        files.deleteAvatar();
    }

    @Override
    public Single<Result<Boolean>> delete(String imageUrl) {
        return network.deleteAvatar(imageUrl);
    }
}
