package com.mdgd.academy2020.cases.auth;

import com.mdgd.academy2020.R;
import com.mdgd.academy2020.cases.UseCase;
import com.mdgd.academy2020.dto.AvatarUpdate;
import com.mdgd.academy2020.dto.UserData;
import com.mdgd.academy2020.models.files.Files;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.prefs.Prefs;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserAuthUseCase implements UseCase<AuthParams, Disposable> {

    private final Network network;
    private final Prefs prefs;
    private final Files files;

    public UserAuthUseCase(Network network, Prefs prefs, Files files) {
        this.network = network;
        this.prefs = prefs;
        this.files = files;
    }

    @Override
    public Disposable exec(AuthParams params) {
        final Single<Result<LoginData>> single;
        switch (params.getType()) {
            case AuthParams.TYPE_SIGN_IN: {
                single = network.createNewUser(params.getEmail(), params.getPassword())
                        .flatMap(result -> {
                            if (result.isFail()) {
                                return Single.just(new Result<>(result.error));
                            } else {
                                return uploadAvatar(params.getImageUrl())
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
                                                                return new Result<>(new LoginData(result.data,
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
                                                return new Result<>(new LoginData(result.data, userResult.data.getImageUrl(),
                                                        downloadAvatar(userResult.data.getImageUrl())));
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
                .doOnSubscribe(disposable -> {
                    if (params.hasView()) {
                        params.getView().showProgress();
                    }
                })
                .doFinally(() -> {
                    if (params.hasView()) {
                        params.getView().hideProgress();
                    }
                })
                .flatMap(result -> {
                    if (result.isFail()) {
                        return Single.just(result);
                    } else {
                        return Completable.fromAction(() -> {
                            prefs.putAuthToken(result.data.token);
                            prefs.putAvatarUrl(result.data.avatarUrl);
                            prefs.putAvatarPath(result.data.avatarPath);
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .toSingleDefault(result);
                    }
                })
                .subscribe(result -> {
                    final AuthView view = params.getView();
                    if (result.isFail() && params.hasView()) {
                        if (result.error == null) {
                            view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                        } else {
                            view.showError(view.getString(R.string.login_failed), result.error.getMessage());
                        }
                    } else {
                        if (params.hasView()) {
                            view.showToast(R.string.login_successful);
                            view.proceedToLobby();
                        }
                    }
                }, error -> {
                    if (params.hasView()) {
                        final AuthView view = params.getView();
                        view.hideProgress();
                        view.showError(view.getString(R.string.login_failed), view.getString(R.string.unknown_error));
                    }
                });
    }

    private String downloadAvatar(String imageUrl) {
        return files.downloadFile(imageUrl);
    }

    private Single<Result<AvatarUpdate>> uploadAvatar(String imageUrl) {
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


    private static class LoginData {
        final String token;
        final String avatarUrl;
        final String avatarPath;

        LoginData(String token, String avatarUrl, String avatarPath) {
            this.token = token;
            this.avatarUrl = avatarUrl;
            this.avatarPath = avatarPath;
        }
    }
}
