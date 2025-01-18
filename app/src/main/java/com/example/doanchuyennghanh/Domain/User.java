package com.example.doanchuyennghanh.Domain;

public class User {
    private String email;
    private String full_name;
    private String id_role;
    private String image_certificate_expert;
    private String image_user;
    private boolean is_active;
    private String phoneNumber;
    private String username;

    public User() {
    }

    public User(String username, String email, String phoneNumber, String full_name, String image_user, String image_certificate_expert, boolean is_active, String id_role) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.full_name = full_name;
        this.image_user = image_user;
        this.image_certificate_expert = image_certificate_expert;
        this.is_active = is_active;
        this.id_role = id_role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getImage_image_certificate_expert() {
        return image_certificate_expert;
    }

    public void setImage_image_certificate_expert(String image_certificate_expert) {
        this.image_certificate_expert = image_certificate_expert;
    }

    public String getId_role() {
        return id_role;
    }

    public void setId_role(String id_role) {
        this.id_role = id_role;
    }
}
