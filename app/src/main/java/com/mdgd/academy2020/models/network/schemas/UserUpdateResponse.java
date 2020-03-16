package com.mdgd.academy2020.models.network.schemas;

public class UserUpdateResponse {
    public final String imagePath;
    public final String imageUrl;

    public UserUpdateResponse(String imagePath, String imageUrl) {
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }
}
