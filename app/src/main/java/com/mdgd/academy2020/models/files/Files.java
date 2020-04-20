package com.mdgd.academy2020.models.files;

import android.net.Uri;

import com.mdgd.academy2020.models.network.Result;

import java.io.File;

public interface Files {

    String downloadFile(String avatarUrl);

    String resolveExtension(String avatarUrl);

    Result<String> getCopyFromPath(String imageUrl);

    Uri getUriFromFile(File file);

    void deleteAvatar();
}
