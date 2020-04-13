package com.mdgd.academy2020.models.repo.user;

import com.google.common.base.Optional;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.models.repo.avatar.AvatarRepository;
import com.mdgd.academy2020.models.repo.user.dao.UserDao;
import com.mdgd.academy2020.models.schemas.UserData;

import io.reactivex.Single;

public class UserRepo implements UserRepository {
    private final AvatarRepository avatarRepo;
    private final Network network;
    private final UserDao userDao;

    public UserRepo(AvatarRepository avatarRepo, Network network, UserDao userDao) {
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
                        return network.updateUser(new UserData(email, nickname, uploadResult.data.imageUrl, avatarRepo.getAvatarHash()))
                                .flatMap(updateResult -> {
                                    if (updateResult.isFail()) {
                                        return Single.just(new Result<>(updateResult.error));
                                    } else {
                                        final User user = new User(email, nickname, uploadResult.data.imageUrl, uploadResult.data.imagePath, uid, avatarRepo.getAvatarHash());
                                        userDao.save(user);
                                        return Single.just(new Result<>(user));
                                    }
                                });
                    }
                });
    }

    @Override
    public Single<Result<User>> getUser(String uid) {
        return Single.fromCallable(() -> Optional.fromNullable(userDao.getUserByUid(uid)))
                .flatMap(userOptional -> {
                    if (userOptional.isPresent()) {
                        return Single.just(new Result<>(userOptional.get()));
                    } else {
                        return network.getUser()
                                .map(userResult -> {
                                    if (userResult.isFail()) {
                                        return new Result<>(userResult.error);
                                    } else {
                                        final UserData data = userResult.data;
                                        final String path = avatarRepo.downloadAvatar(data.getImageUrl());
                                        final User user = new User(data.getEmail(), data.getNickname(), data.getImageUrl(), path, uid, data.getAvatarHash());
                                        userDao.save(user);
                                        avatarRepo.putAvatarHash(data.getAvatarHash());
                                        return new Result<>(user);
                                    }
                                });
                    }
                });
    }

    @Override
    public void clear() {
        userDao.deleteAll();
    }
}
