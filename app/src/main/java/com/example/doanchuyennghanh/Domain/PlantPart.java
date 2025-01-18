package com.example.doanchuyennghanh.Domain;

import java.io.Serializable;

public class PlantPart implements Serializable {
    private int Id;
    private String TenBoPhanDung;

    public PlantPart() {
    }

    public PlantPart(int id, String tenBoPhanDung) {
        Id = id;
        TenBoPhanDung = tenBoPhanDung;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTenBoPhanDung() {
        return TenBoPhanDung;
    }

    public void setTenBoPhanDung(String tenBoPhanDung) {
        TenBoPhanDung = tenBoPhanDung;
    }
}
