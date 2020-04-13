package com.mdgd.academy2020.util;

import android.widget.ImageView;

import com.mdgd.academy2020.models.repo.user.User;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageUtil {

    public static void loadImage(ImageView imageView, String pathToImage) {
        Picasso.get().load(pathToImage).into(imageView);
    }

    public static void loadImage(ImageView imageView, User user) {
        final File file = new File(user.getImagePath());
        if (file.exists()) {
            Picasso.get().load(file).into(imageView);
        } else if (TextUtil.isEmpty(user.getImageUrl())) {
            Picasso.get().load(user.getImageUrl()).into(imageView);
        }
    }
}
