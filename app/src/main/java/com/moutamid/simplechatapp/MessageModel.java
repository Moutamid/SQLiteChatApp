package com.moutamid.simplechatapp;

public class MessageModel {
    public String message,image;
    public int sent_by, id;
    public boolean isSelected = false;

    public MessageModel(int id, String message, int sent_by, String image) {
        this.message = message;
        this.sent_by = sent_by;
        this.image = image;
        this.id = id;
    }

    public MessageModel() {
    }
}
