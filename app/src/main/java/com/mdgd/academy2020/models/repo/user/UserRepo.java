package com.mdgd.academy2020.models.repo.user;

import com.mdgd.academy2020.models.dao.Dao;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.repo.avatar.AvatarRepository;
import com.mdgd.academy2020.models.schemas.UserData;

import io.reactivex.Single;

public class UserRepo implements UserRepository {
    private final AvatarRepository avatarRepo;
    private final Network network;
    private final Dao<User> userDao;

    public UserRepo(AvatarRepository avatarRepo, Network network, Dao<User> userDao) {
        this.avatarRepo = avatarRepo;
        this.network = network;
        this.userDao = userDao;
    }

    @Override
    public Single<Result<User>> createNewUser(String uid, String email, String imageUrl, String nickname) {
        return avatarRepo.uploadAvatar(imageUrl)
                .flatMap(uploadResult -> {
                    if (uploadResult.isFail()) {
                        return Single.just(new Result<>(uploadResult.error));
                    } else {
                        return network.updateUser(new UserData(email, nickname, uploadResult.data.imageUrl))
                                .flatMap(updateResult -> {
                                    if (updateResult.isFail()) {
                                        return Single.just(new Result<>(updateResult.error));
                                    } else {
                                        final User user = new User(email, nickname, uploadResult.data.imageUrl, uploadResult.data.imagePath);
                                        userDao.save(user);
                                        return Single.just(new Result<>(user));
                                    }
                                });
                    }
                });
    }

    @Override
    public Single<Result<User>> loadUser(String uid) {
        return network.getUser()
                .map(userResult -> {
                    if (userResult.isFail()) {
                        return new Result<>(userResult.error);
                    } else {
                        final UserData data = userResult.data;
                        final String path = avatarRepo.downloadAvatar(data.getImageUrl());
                        final User user = new User(data.getEmail(), data.getNickname(), data.getImageUrl(), path);
                        userDao.save(user);
                        return new Result<>(user);
                    }
                });
    }

    @Override
    public void clear() {
        userDao.deleteAll();
    }
}
