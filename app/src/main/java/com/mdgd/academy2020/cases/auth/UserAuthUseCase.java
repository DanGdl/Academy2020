package com.mdgd.academy2020.cases.auth;

import com.mdgd.academy2020.cases.UseCase;
import com.mdgd.academy2020.dto.UserData;
import com.mdgd.academy2020.models.avatar.AvatarRepository;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserAuthUseCase implements UseCase<AuthParams, Single<Result<AuthResult>>> {

    private final Network network;
    private final Prefs prefs;
    private final AvatarRepository avatarRepo;

    public UserAuthUseCase(Network network, Prefs prefs, AvatarRepository avatarRepo) {
        this.network = network;
        this.prefs = prefs;
        this.avatarRepo = avatarRepo;
    }

    @Override
    public Single<Result<AuthResult>> exec(AuthParams params) {
        final Single<Result<AuthResult>> single;
        switch (params.getType()) {
            case AuthParams.TYPE_SIGN_IN: {
                single = network.createNewUser(params.getEmail(), params.getPassword())
                        .flatMap(result -> {
                            if (result.isFail()) {
                                return Single.just(new Result<>(result.error));
                            } else {
                                return avatarRepo.uploadAvatar(params.getImageUrl())
                                        .flatMap(uploadResult -> {
                                            if (uploadResult.isFail()) {
                                                return Single.just(new Result<>(uploadResult.error));
                                            } else {
                                                return network.updateUser(new UserData(params.getEmail(),
                                                        params.getNickName(), uploadResult.data.imageUrl))
                                                        .map(updateResult -> {
                                                            if (updateResult.isFail()) {
                                                                return new Result<>(updateResult.error);
                                                            } else {
                                                                return new Result<>(new AuthResult(result.data,
                                                                        uploadResult.data.imageUrl, uploadResult.data.imagePath));
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });
            }
            break;

            case AuthParams.TYPE_LOG_IN: {
                single = network.execLogin(params.getEmail(), params.getPassword())
                        .flatMap(result -> {
                            if (result.isFail()) {
                                return Single.just(new Result<>(result.error));
                            } else {
                                return network.getUser()
                                        .map(userResult -> {
                                            if (userResult.isFail()) {
                                                return new Result<>(userResult.error);
                                            } else {
                                                return new Result<>(new AuthResult(result.data, userResult.data.getImageUrl(),
                                                        avatarRepo.downloadAvatar(userResult.data.getImageUrl())));
                                            }
                                        });
                            }
                        });
            }
            break;

            default:
                single = Single.never();
        }
        return single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(result -> {
                    if (result.isFail()) {
                        return Single.just(result);
                    } else {
                        return Completable.fromAction(() -> {
                            prefs.putAuthToken(result.data.token);
                            avatarRepo.putAvatarUrl(result.data.avatarUrl);
                            avatarRepo.putAvatarPath(result.data.avatarPath);
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .toSingleDefault(result);
                    }
                });
    }
}
