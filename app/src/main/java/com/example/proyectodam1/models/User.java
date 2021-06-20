package com.example.proyectodam1.models;

public class User {

    private String id;
    private String userName;
    private String cel;
    private String image;

    public User() {
    }

    public User(String id, String userName, String cel, String image) {
        this.id = id;
        this.userName = userName;
        this.cel = cel;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
