package com.freedom.pjeditor.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Mayur on 6/27/2017.
 */

public class SelectedImages extends RealmObject {

    public static String IMAGE_ID = "imageId";

    @PrimaryKey
    private int imageId;
    private String imagePath;
    private String croppedImageCoOrdinates;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCroppedImageCoOrdinates() {
        return croppedImageCoOrdinates;
    }

    public void setCroppedImageCoOrdinates(String croppedImageCoOrdinates) {
        this.croppedImageCoOrdinates = croppedImageCoOrdinates;
    }
}