package com.example.proyectodam1.models;

import java.util.ArrayList;

public class Chat {
    private String id;
    private String writing;
    private long timestamp;
    private ArrayList<String> ids;
    private int numberMessage;

    public Chat() {
    }

    public Chat(String id, String writing, long timestamp, ArrayList<String> ids, int numberMessage) {
        this.id = id;
        this.writing = writing;
        this.timestamp = timestamp;
        this.ids = ids;
        this.numberMessage = numberMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public int getNumberMessage() {
        return numberMessage;
    }

    public void setNumberMessage(int numberMessage) {
        this.numberMessage = numberMessage;
    }
}
