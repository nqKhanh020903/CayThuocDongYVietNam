package com.example.doanchuyennghanh.Domain;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class Herb implements Serializable {
    private int Id;
    private String TenThuoc;
    private String TenKhoaHoc;
    private String Ho;
    private List<String> BoPhanDung;
    private List<String> ThanhPhanHoaHocChinh;
    private String CongDung;
    private String CachDungVaLieuLuong;
    private String PhanBo;
    private String MoTaCay;
    private List<Image> Image;
    private List<Integer> HashTagId;
    private List<Integer> BoPhanDungId;
    private List<Integer> DanhMucId;

    public Herb(List<Integer> danhMucId) {
        DanhMucId = danhMucId;
    }

    public Herb() {}

    public Herb(String phanBo, String moTaCay) {
        PhanBo = phanBo;
        MoTaCay = moTaCay;
    }

    public Herb(int id, String tenThuoc, String tenKhoaHoc, String ho, List<String> boPhanDung, List<String> thanhPhanHoaHocChinh,
                String congDung, String cachDungVaLieuLuong, List<com.example.doanchuyennghanh.Domain.Image> image, List<Integer> hashTagId, List<Integer> boPhanDungId) {
        Id = id;
        TenThuoc = tenThuoc;
        TenKhoaHoc = tenKhoaHoc;
        Ho = ho;
        BoPhanDung = boPhanDung;
        ThanhPhanHoaHocChinh = thanhPhanHoaHocChinh;
        CongDung = congDung;
        CachDungVaLieuLuong = cachDungVaLieuLuong;
        Image = image;
        HashTagId = hashTagId;
        BoPhanDungId = boPhanDungId;
    }

    @Override
    public String toString() {
        return TenThuoc;
    }

    public List<Integer> getDanhMucId() {
        return DanhMucId;
    }

    public String getPhanBo() {
        return PhanBo;
    }

    public void setPhanBo(String phanBo) {
        PhanBo = phanBo;
    }

    public String getMoTaCay() {
        return MoTaCay;
    }

    public void setMoTaCay(String moTaCay) {
        MoTaCay = moTaCay;
    }

    public void setDanhMucId(List<Integer> danhMucId) {
        DanhMucId = danhMucId;
    }

    public List<Integer> getBoPhanDungId() {
        return BoPhanDungId;
    }

    public void setBoPhanDungId(List<Integer> boPhanDungId) {
        BoPhanDungId = boPhanDungId;
    }

    public List<Integer> getHashTagId() {
        return HashTagId;
    }

    public void setHashTagId(List<Integer> hashTagId) {
        HashTagId = hashTagId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTenThuoc() {
        return TenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        TenThuoc = tenThuoc;
    }

    public String getTenKhoaHoc() {
        return TenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        TenKhoaHoc = tenKhoaHoc;
    }

    public String getHo() {
        return Ho;
    }

    public void setHo(String ho) {
        Ho = ho;
    }

    public List<String> getBoPhanDung() {
        return BoPhanDung;
    }

    public void setBoPhanDung(List<String> boPhanDung) {
        BoPhanDung = boPhanDung;
    }

    public List<String> getThanhPhanHoaHocChinh() {
        return ThanhPhanHoaHocChinh;
    }

    public void setThanhPhanHoaHocChinh(List<String> thanhPhanHoaHocChinh) {
        ThanhPhanHoaHocChinh = thanhPhanHoaHocChinh;
    }

    public String getCongDung() {
        return CongDung;
    }

    public void setCongDung(String congDung) {
        CongDung = congDung;
    }

    public String getCachDungVaLieuLuong() {
        return CachDungVaLieuLuong;
    }

    public void setCachDungVaLieuLuong(String cachDungVaLieuLuong) {
        CachDungVaLieuLuong = cachDungVaLieuLuong;
    }

    // Hàm getImage() để trả về ImagePath của hình ảnh có Id là 1
    public String getImage() {
        for (Image image : Image) {
            if (image.getId() == 1) {
                return image.getImagePath();
            }
        }
        return null; // Hoặc một URL mặc định nếu không tìm thấy
    }

    public List<Image> getImageList() {
        return Image; // Trả về toàn bộ danh sách ảnh
    }


    public void setImage(List<Image> image) {
        Image = image;
    }

    protected Herb(Parcel in) {
        TenThuoc = in.readString();
        CongDung = in.readString();
        TenKhoaHoc = in.readString();
        //Image = in.readString();
    }

    public static final Parcelable.Creator<Herb> CREATOR = new Parcelable.Creator<Herb>() {
        @Override
        public Herb createFromParcel(Parcel in) {
            return new Herb(in);
        }

        @Override
        public Herb[] newArray(int size) {
            return new Herb[size];
        }
    };


}
