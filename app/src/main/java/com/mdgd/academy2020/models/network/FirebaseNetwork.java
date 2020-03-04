package com.mdgd.academy2020.models.network;

import android.app.Application;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.mdgd.academy2020.BuildConfig;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Single;

public class FirebaseNetwork implements Network {

    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebase;
    private final Application app;

    public FirebaseNetwork(Application app) {
        this.app = app;
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
        return Single.just(firebaseAuth.createUserWithEmailAndPassword(email, password))
                .flatMap(task -> {
                    if (task.isSuccessful()) {
                        return execLogin(email, password)
                                .flatMap(result -> {
                                    if (result.isFail()) {
                                        return Single.just(result);
                                    } else {
                                        return updateUser(nickname, imageUrl)
                                                .toSingleDefault(result);
                                    }
                                });
                    } else {
                        return Single.just(new Result<>(task.getException()));
                    }
                });
    }

    private Completable updateUser(String nickname, String imageUrl) {
        // todo save nickname and image
        return uploadImage() Single.just(firebase.collection("users").add());
    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }

    public Completable uploadImage(File file, String fileName) {
        return Single.just(FirebaseStorage.getInstance()
                .getReference().child("icons/" + fileName + ".webp").putFile(getUriFromFile(file)))
                .ignoreElement();
    }

    private Uri getUriFromFile(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(app, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
