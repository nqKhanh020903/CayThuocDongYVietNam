package com.example.doanchuyennghanh.Domain;

import java.io.Serializable;

public class Image implements Serializable {
    private int Id;
    private String ImagePath;

    public Image() {
    }

    public Image(int id, String imagePath) {
        Id = id;
        ImagePath = imagePath;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
