package com.example.doanchuyennghanh.Domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable {
    private int Id;
    private double Star;
    private String TieuDe;
    private String TacGia;
    private String ThamVanYKhoa;
    private String NgayDang;
    private String Image;
    private List<Integer> CayThuocId;
    private String Nguon;
    private String TomTatNoiDung;

    public Post() {
    }

    public Post(int id, double star, String tieuDe, String tacGia, String thamVanYKhoa, String ngayDang, String image, List<Integer> cayThuocId, String nguon, String tomTatNoiDung) {
        Id = id;
        Star = star;
        TieuDe = tieuDe;
        TacGia = tacGia;
        ThamVanYKhoa = thamVanYKhoa;
        NgayDang = ngayDang;
        Image = image;
        CayThuocId = cayThuocId;
        Nguon = nguon;
        TomTatNoiDung = tomTatNoiDung;
    }



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
    }

    public String getTacGia() {
        return TacGia;
    }

    public void setTacGia(String tacGia) {
        TacGia = tacGia;
    }

    public String getThamVanYKhoa() {
        return ThamVanYKhoa;
    }

    public void setThamVanYKhoa(String thamVanYKhoa) {
        ThamVanYKhoa = thamVanYKhoa;
    }

    public String getNgayDang() {
        return NgayDang;
    }

    public void setNgayDang(String ngayDang) {
        NgayDang = ngayDang;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public List<Integer> getCayThuocId() {
        return CayThuocId;
    }

    public void setCayThuocId(List<Integer> cayThuocId) {
        CayThuocId = cayThuocId;
    }

    public String getNguon() {
        return Nguon;
    }

    public void setNguon(String nguon) {
        Nguon = nguon;
    }

    public String getTomTatNoiDung() {
        return TomTatNoiDung;
    }

    public void setTomTatNoiDung(String tomTatNoiDung) {
        TomTatNoiDung = tomTatNoiDung;
    }
}
