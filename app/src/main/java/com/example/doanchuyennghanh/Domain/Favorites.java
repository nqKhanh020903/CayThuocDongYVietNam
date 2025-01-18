package com.example.doanchuyennghanh.Domain;

import java.time.LocalDate;

public class Favorites {
    private int id;
    private int id_cayThuoc;
    private int id_user;
    private LocalDate create_at;

    public Favorites() {
    }

    public Favorites(int id, int id_cayThuoc, int id_user, LocalDate create_at) {
        this.id = id;
        this.id_cayThuoc = id_cayThuoc;
        this.id_user = id_user;
        this.create_at = create_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_cayThuoc() {
        return id_cayThuoc;
    }

    public void setId_cayThuoc(int id_cayThuoc) {
        this.id_cayThuoc = id_cayThuoc;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public LocalDate getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDate create_at) {
        this.create_at = create_at;
    }
}
