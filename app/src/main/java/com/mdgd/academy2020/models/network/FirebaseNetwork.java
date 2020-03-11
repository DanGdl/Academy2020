package com.mdgd.academy2020.models.network;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
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

    private <T> Single<Result<T>> execRequest(Task<T> task) {
        return Single.fromCallable(() -> {
            try {
                return new Result<>(Tasks.await(task));
            } catch (Throwable e) {
                e.printStackTrace();
                return new Result<>(e);
            }
        });
    }

    @Override
    public boolean hasUser() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public Single<Result<String>> execLogin(String email, String password) {
        return execRequest(firebaseAuth.signInWithEmailAndPassword(email, password))
                .flatMap(result -> {
                    if (result.isFail()) {
                        Log.d("FirebaseNetwork", "login Has error " + (result.error != null));
                        return Single.just(new Result<>(result.error));
                    } else {
                        return Single.fromCallable(() -> {
                            try {
                                final GetTokenResult await = Tasks.await(firebaseAuth.getCurrentUser().getIdToken(true));
                                return new Result<>(await.getToken());
                            } catch (Throwable e) {
                                e.printStackTrace();
                                return new Result<>(e);
                            }
                        });
                    }
                });
    }

    @Override
    public Single<Result<String>> createNewUser(String nickname, String email, String
            password, String imageUrl) {
        return execRequest(firebaseAuth.createUserWithEmailAndPassword(email, password))
                .flatMap(result -> {
                    if (result.isFail()) {
                        Log.d("FirebaseNetwork", "new User Has error " + (result.error != null));
                        return Single.just(new Result<>(result.error));
                    } else {
                        return execLogin(email, password)
                                .flatMap(loginResult -> {
                                    if (loginResult.isFail()) {
                                        Log.d("FirebaseNetwork", "login2 Has error " + (loginResult.error != null));
                                        return Single.just(loginResult);
                                    } else {
                                        return updateUser(nickname, imageUrl)
                                                .toSingleDefault(loginResult);
                                    }
                                });
                    }
                });
    }

    private Completable updateUser(String nickname, String imageUrl) {
        // todo save nickname and image
        return Completable.complete(); // uploadImage() Single.just(firebase.collection("users").add());
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
