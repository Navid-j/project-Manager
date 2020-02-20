package com.example.projectmanager.Model;

public class UserModel {
    private String userName;
    private String userId;

    public UserModel(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }
}
