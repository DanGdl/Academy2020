package com.mdgd.academy2020.cases;

import com.google.common.base.Optional;
import com.mdgd.academy2020.models.network.Network;
import com.mdgd.academy2020.models.prefs.Prefs;
import com.mdgd.academy2020.models.repo.user.UserRepository;

import io.reactivex.Notification;
import io.reactivex.Single;

public class LogoutUseCase implements UseCase<Object, Single<Notification>> {

    private final Network network;
    private final Prefs prefs;
    private final UserRepository userRepo;

    public LogoutUseCase(Network network, Prefs prefs, UserRepository userRepo) {
        this.network = network;
        this.prefs = prefs;
        this.userRepo = userRepo;
    }

    @Override
    public Single<Notification> exec(Object o) {
        return Single.fromCallable(() -> {
            network.logOut();
            prefs.putAuthToken("");
            userRepo.clear();
            return Notification.createOnNext(Optional.absent());
        });
    }
}
