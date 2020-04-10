package com.mdgd.academy2020.models.network;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mdgd.academy2020.models.files.Files;
import com.mdgd.academy2020.models.schemas.AvatarUpdate;
import com.mdgd.academy2020.models.schemas.UserData;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;

public class FirebaseNetwork implements Network {

    private static final String USERS = "users";
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebase;
    private final Files files;

    public FirebaseNetwork(Files files) {
        this.files = files;
        firebaseAuth = FirebaseAuth.getInstance();
        firebase = FirebaseFirestore.getInstance();
    }

    private <T> Single<Result<T>> execRequestRx(Task<T> task) {
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
    public Single<Result<String>> loginUser(String email, String password) {
        return execLoginInner(email, password);
    }

    @Override
    public Single<Result<String>> loginNewUser(String email, String password) {
        return execRequestRx(firebaseAuth.createUserWithEmailAndPassword(email, password))
                .flatMap(result -> {
                    if (result.isFail()) {
                        return Single.just(new Result<>(result.error));
                    } else {
                        return updateUser(new UserData(email))
                                .flatMap(userResult -> {
                                    if (userResult.isFail()) {
                                        return Single.just(new Result<>(userResult.error));
                                    } else {
                                        return execLoginInner(email, password);
                                    }
                                });
                    }
                });
    }

    private Single<Result<String>> execLoginInner(String email, String password) {
        return execRequestRx(firebase.collection(USERS).whereEqualTo("email", email).get())
                .flatMap(snapshot -> {
                    if (snapshot.isFail()) {
                        return Single.just(new Result<>(snapshot.error));
                    } else if (snapshot.data.getDocuments().isEmpty()) {
                        return Single.just(new Result<>(new Exception("User with this mail does not exist")));
                    } else {
                        return execRequestRx(firebaseAuth.signInWithEmailAndPassword(email, password))
                                .map(result -> {
                                    if (result.isFail()) {
                                        return new Result<>(result.error);
                                    } else {
                                        return new Result<>(firebaseAuth.getCurrentUser().getUid());
                                    }
                                });
                    }
                });
    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }

    public Single<Result<AvatarUpdate>> uploadImage(String imagePath) {
        final Uri parse = files.getUriFromFile(new File(imagePath));
        final String[] split = parse.getLastPathSegment().split("\\.");
        final StorageReference child = FirebaseStorage.getInstance().getReference()
                .child("icons/" + split[0] + "_" + System.currentTimeMillis() + "." + split[1]);
        return execRequestRx(child.putFile(parse))
                .flatMap(result -> {
                    if (result.isFail()) {
                        return Single.just(new Result<>(result.error));
                    } else {
                        return execRequestRx(child.getDownloadUrl())
                                .map(urlResult -> {
                                    if (urlResult.isFail()) {
                                        return new Result<>(urlResult.error);
                                    } else {
                                        return new Result<>(new AvatarUpdate(urlResult.data.toString(), imagePath));
                                    }
                                });
                    }
                });
    }

    @Override
    public Single<Result<Boolean>> updateUser(UserData data) {
        final Map<String, Object> params = new HashMap<>();
        params.put("email", data.getEmail());
        params.put("nickname", data.getNickname());
        params.put("avatar", data.getImageUrl());
        return execRequestRx(firebase.collection(USERS).document(firebaseAuth.getCurrentUser().getUid()).set(params, SetOptions.merge()))
                .map(updateResult -> {
                    if (updateResult.isFail()) {
                        return new Result<>(updateResult.error);
                    } else {
                        return new Result<>(true);
                    }
                });
    }

    @Override
    public Single<Result<UserData>> getUser() {
        return execRequestRx(firebase.collection(USERS).document(firebaseAuth.getCurrentUser().getUid()).get())
                .map(userResult -> {
                    if (userResult.isFail()) {
                        return new Result<>(userResult.error);
                    } else {
                        return new Result<>(new UserData(userResult.data.getString("email"),
                                userResult.data.getString("nickname"),
                                userResult.data.getString("avatar")));
                    }
                });
    }
}
