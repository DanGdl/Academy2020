package com.mdgd.academy2020.models.network;

import android.app.Application;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mdgd.academy2020.BuildConfig;
import com.mdgd.academy2020.dto.LoginResponse;
import com.mdgd.academy2020.models.network.schemas.UserUpdateResponse;
import com.mdgd.academy2020.util.TextUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;

public class FirebaseNetwork implements Network {

    private static final String USERS = "users";
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
    public Single<Result<LoginResponse>> execLogin(String email, String password) {
        return execLoginInner(email, password, "", "");
    }

    @Override
    public Single<Result<LoginResponse>> createNewUser(String nickname, String email, String
            password, String imageUrl) {
        return execRequest(firebaseAuth.createUserWithEmailAndPassword(email, password))
                .flatMap(result -> {
                    if (result.isFail()) {
                        Log.d("FirebaseNetwork", "new User Has error " + (result.error != null));
                        return Single.just(new Result<>(result.error));
                    } else {
                        return updateUser(email, nickname, imageUrl)
                                .flatMap(updateResult -> {
                                    if (result.isFail()) {
                                        return Single.just(new Result<>(result.error));
                                    } else {
                                        return execLoginInner(email, password, updateResult.data.imageUrl, updateResult.data.imagePath);
                                    }
                                });
                    }
                });
    }

    private Single<Result<LoginResponse>> execLoginInner(String email, String password, String avatarUrl, String avatarPath) {
        return execRequest(firebase.collection(USERS).whereEqualTo("email", email).get())
                .flatMap(snapshot -> {
                    if (snapshot.isFail()) {
                        Log.d("FirebaseNetwork", "check email " + (snapshot.error != null));
                        return Single.just(new Result<>(snapshot.error));
                    } else if (snapshot.data.getDocuments().isEmpty()) {
                        return Single.just(new Result<>(new Exception("User with this mail does not exist")));
                    } else {
                        final String avatarUrlFinal;
                        if (TextUtil.isEmpty(avatarUrl)) {
                            final DocumentSnapshot userSnapshot = snapshot.data.getDocuments().get(0);
                            avatarUrlFinal = (String) userSnapshot.get("avatar");
                        } else {
                            avatarUrlFinal = avatarUrl;
                        }
                        final String avatarPathFinal;
                        if (TextUtil.isEmpty(avatarPath)) {
                            // todo download file
                            avatarPathFinal = "";
                        } else {
                            avatarPathFinal = avatarPath;
                        }
                        return execRequest(firebaseAuth.signInWithEmailAndPassword(email, password))
                                .flatMap(result -> {
                                    if (result.isFail()) {
                                        Log.d("FirebaseNetwork", "login Has error " + (result.error != null));
                                        return Single.just(new Result<>(result.error));
                                    } else {
                                        return execRequest(firebaseAuth.getCurrentUser().getIdToken(true))
                                                .flatMap(tokenResult -> {
                                                    if (result.isFail()) {
                                                        Log.d("FirebaseNetwork", "get token Has error " + (tokenResult.error != null));
                                                        return Single.just(new Result<>(tokenResult.error));
                                                    } else {
                                                        return Single.just(new Result<>(new LoginResponse(tokenResult.data.getToken(), avatarUrlFinal, avatarPathFinal)));
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    private Single<Result<UserUpdateResponse>> updateUser(String email, String nickname, String imageUrl) {
        final Single<Result<String>> imageUpload;
        final String imagePath;
        if (imageUrl.contains("http")) {
            // // todo download to file
            imagePath = imageUrl;
            imageUpload = uploadImage(imageUrl);
        } else {
            imagePath = imageUrl;
            imageUpload = uploadImage(imageUrl);
        }
        return imageUpload.flatMap(result -> {
            if (result.isFail()) {
                return Single.just(new Result<>(result.error));
            } else {
                final Map<String, Object> params = new HashMap<>();
                params.put("email", email);
                params.put("nickname", nickname);
                params.put("avatar", result.data);
                return execRequest(firebase.collection(USERS).document().set(params, SetOptions.merge()))
                        .map(ignore -> new Result<>(new UserUpdateResponse(imagePath, imageUrl)));
            }
        });
    }

    @Override
    public void logOut() {
        firebaseAuth.signOut();
    }

    public Single<Result<String>> uploadImage(String imageUrl) {
        final Uri parse = Uri.parse(imageUrl);
        final File file = new File(parse.getPath());
        final String[] split = file.getName().split("\\.");
        final StorageReference child = FirebaseStorage.getInstance().getReference()
                .child("icons/" + split[0] + "_" + System.currentTimeMillis() + "." + split[1]);
        return execRequest(child.putFile(parse))
                .flatMap(result -> {
                    if (result.isFail()) {
                        return Single.just(new Result<>(result.error));
                    } else {
                        return execRequest(child.getDownloadUrl());
                    }
                })
                .map(result -> {
                    if (result.isFail()) {
                        return new Result<>(result.error);
                    } else {
                        return new Result<>(result.data.toString());
                    }
                });
    }

    private Uri getUriFromFile(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(app, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
