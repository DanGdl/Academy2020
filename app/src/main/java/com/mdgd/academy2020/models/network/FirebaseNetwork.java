package com.mdgd.academy2020.models.network;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.Single;

public class FirebaseNetwork implements Network {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebase;

    public FirebaseNetwork(Application app) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebase = FirebaseFirestore.getInstance();
    }

    @Override
    public boolean hasUser() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public Single<Result<String>> execLogin(String email, String password) {
        return Single.just(firebaseAuth.signInWithEmailAndPassword(email, password))
                .flatMap(task -> {
                    if (task.isSuccessful()) {
                        return Single.just(firebaseAuth.getCurrentUser().getIdToken(true))
                                .map(tokenTask -> {
                                    if (tokenTask.isSuccessful()) {
                                        return new Result<>(tokenTask.getResult().getToken());
                                    } else {
                                        return new Result<>(tokenTask.getException());
                                    }
                                });
                    } else {
                        return Single.just(new Result<>(task.getException()));
                    }
                });
    }

    @Override
    public Single<Result<String>> createNewUser(String nickname, String email, String password, String imageUrl) {
        // todo save nickname and image
        return Single.just(firebaseAuth.createUserWithEmailAndPassword(email, password))
                .flatMap(task -> {
                    if (task.isSuccessful()) {
                        return execLogin(email, password);
                    } else {
                        return Single.just(new Result<>(task.getException()));
                    }
                });
    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }
}
