package com.mdgd.academy2020.models.repo.user;

import com.google.common.base.Optional;
import com.mdgd.academy2020.models.cache.profile.ProfileCache;
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
    private final ProfileCache profileCache;

    public UserRepo(AvatarRepository avatarRepo, Network network, UserDao userDao, ProfileCache profileCache) {
        this.avatarRepo = avatarRepo;
        this.network = network;
        this.userDao = userDao;
        this.profileCache = profileCache;
    }

    @Override
    public Single<Result<User>> getUser(String uid) {
        return Single.fromCallable(() -> Optional.fromNullable(userDao.getUserByUid(uid)))
                .flatMap(userOptional -> {
                    if (userOptional.isPresent()) {
                        profileCache.putUser(userOptional.get());
                        return Single.just(new Result<>(userOptional.get()));
                    } else {
                        return network.getUser()
                                .map(userResult -> {
                                    if (userResult.isFail()) {
                                        return new Result<>(userResult.error);
                                    } else {
                                        final UserData data = userResult.data;
                                        final String path = avatarRepo.downloadAvatar(data.getImageUrl());
                                        final User user = new User(data.getEmail(), data.getNickname(), data.getImageUrl(), path, uid, data.getAvatarHash(), data.getAvatarType());
                                        userDao.save(user);
                                        profileCache.putUser(user);
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

    @Override
    public Single<Result<User>> save(User user) {
        return avatarRepo.uploadAvatar(user.getImageUrl())
                .flatMap(uploadResult -> {
                    if (uploadResult.isFail()) {
                        return Single.just(new Result<>(uploadResult.error));
                    } else {
                        user.setImageUrl(uploadResult.data.imageUrl);
                        user.setImagePath(uploadResult.data.imagePath);
                        return network.updateUser(new UserData(user.getEmail(), user.getNickname(), user.getImageUrl(), user.getAvatarHash(), user.getAvatarType()))
                                .flatMap(updateResult -> {
                                    if (updateResult.isFail()) {
                                        return Single.just(new Result<>(updateResult.error));
                                    } else {
                                        userDao.save(user);
                                        return Single.just(new Result<>(user));
                                    }
                                });
                    }
                });
    }
}
