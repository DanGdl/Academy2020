package com.mdgd.academy2020.util;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtil {

    public static void loadImage(ImageView imageView, String pathToImage) {
        Picasso.get().load(pathToImage).into(imageView);
    }
}
