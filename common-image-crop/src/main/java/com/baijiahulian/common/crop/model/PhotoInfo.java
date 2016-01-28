package com.baijiahulian.common.crop.model;

import java.io.Serializable;

/**
 * Created by yanglei on 16/1/22.
 */
public class PhotoInfo implements Serializable {

    private int photoId;
    private String photoPath;

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
