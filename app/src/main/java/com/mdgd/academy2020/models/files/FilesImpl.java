package com.mdgd.academy2020.models.files;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.mdgd.academy2020.BuildConfig;
import com.mdgd.academy2020.models.network.Result;
import com.mdgd.academy2020.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FilesImpl implements Files {
    private final Application app;

    public FilesImpl(Application app) {
        this.app = app;
    }

    @Override
    public String downloadFile(String avatarUrl) {
        final String fileName = "avatar." + (avatarUrl.contains("firebase") ? resolveExtension(avatarUrl) : "png");
        try {
            final URL url = new URL(avatarUrl);
            final URLConnection connection = url.openConnection();
            connection.connect();

            try (final InputStream input = new BufferedInputStream(url.openStream());
                 final FileOutputStream output = app.openFileOutput(fileName, Context.MODE_PRIVATE)) {

                FileUtil.copy(input, output);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return app.getFilesDir().getPath() + "/" + fileName;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String resolveExtension(String avatarUrl) {
        // https://firebasestorage.googleapis.com/v0/b/academy2020-550e5.appspot.com/o/icons%2Favatar_1584466985338.png?alt=media&token=99b8d975-059f-4fe8-a29c-0172a5677faa
        final int idx;
        if (avatarUrl.contains("?")) {
            final int i = avatarUrl.indexOf("?");
            idx = avatarUrl.substring(0, i).lastIndexOf(".");
            return avatarUrl.substring(idx + 1, i);
        } else {
            idx = avatarUrl.lastIndexOf(".");
            return avatarUrl.substring(idx + 1);
        }
    }

    @Override
    public Result<String> getCopyFromPath(String imageUrl) {
        final int idx = imageUrl.lastIndexOf(".");
        final String imagePath = app.getFilesDir().getPath() + "/avatar" + imageUrl.substring(idx);
        try {
            FileUtil.copy(app, imageUrl, imagePath);
        } catch (Throwable e) {
            return new Result<>(e);
        }
        return new Result<>(imagePath);
    }

    @Override
    public Uri getUriFromFile(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(app, BuildConfig.APPLICATION_ID, file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
