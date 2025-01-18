package com.example.doanchuyennghanh.Domain;

public class Comment {
    private int CayThuocId;
    private String UserId;
    private String UserName;
    private String CommentText;
    private String Date;

    public Comment() {
        // Default constructor
    }

    public Comment(int cayThuocId, String userId, String userName, String commentText, String date) {
        CayThuocId = cayThuocId;
        UserId = userId;
        UserName = userName;
        CommentText = commentText;
        Date = date;
    }

    public int getCayThuocId() {
        return CayThuocId;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getCommentText() {
        return CommentText;
    }

    public String getDate() {
        return Date;
    }
}


