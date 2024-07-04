package com.example.nt118_appbookstores.Models;


public class UserModel{

    private static String userID;
    String name ;
    String phone ;
    String email ;
    String password;
    String profileImg ;

    public UserModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserModel(String name, String phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public UserModel() {
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        UserModel.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
