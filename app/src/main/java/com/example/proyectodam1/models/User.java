package com.example.proyectodam1.models;

public class User {

    private String id;
    private String userName;
    private String cel;
    private String image;
    private String info;
    private long lastConnect;
    private boolean online;

    public User() {
    }

    public User(String id, String userName, String cel, String image, String info, long lastConnect, boolean online) {
        this.id = id;
        this.userName = userName;
        this.cel = cel;
        this.image = image;
        this.info = info;
        this.lastConnect = lastConnect;
        this.online = online;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getLastConnect() {
        return lastConnect;
    }

    public void setLastConnect(long lastConnect) {
        this.lastConnect = lastConnect;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
