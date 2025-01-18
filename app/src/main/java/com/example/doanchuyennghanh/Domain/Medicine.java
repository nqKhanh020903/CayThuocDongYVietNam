package com.example.doanchuyennghanh.Domain;

public class Medicine {
    private int imageResource;
    private String title;
    private String description;
    private String temperature;

    public Medicine(int imageResource, String title, String description, String temperature) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
        this.temperature = temperature;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTemperature() {
        return temperature;
    }
}
