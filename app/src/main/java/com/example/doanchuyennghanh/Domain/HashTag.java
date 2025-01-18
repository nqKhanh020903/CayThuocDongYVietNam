package com.example.doanchuyennghanh.Domain;

import java.io.Serializable;

public class HashTag implements Serializable {
    private int Id;
    private String TenHashTag;

    public HashTag() {
    }

    public HashTag(int Id, String tenHashTag) {
        this.Id = Id;
        TenHashTag = tenHashTag;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTenHashTag() {
        return TenHashTag;
    }

    public void setTenHashTag(String tenHashTag) {
        TenHashTag = tenHashTag;
    }
}
