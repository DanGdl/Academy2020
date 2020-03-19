package com.mdgd.academy2020.dto;

public class AvatarUpdate {
    public final String imagePath;
    public final String imageUrl;

    public AvatarUpdate(String imageUrl, String imagePath) {
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }
}
