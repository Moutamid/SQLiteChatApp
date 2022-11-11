package com.moutamid.simplechatapp;

public class ChatModel {
    public String name, last_message;
    public int image;

    public ChatModel(String name, String last_message, int image) {
        this.name = name;
        this.last_message = last_message;
        this.image = image;
    }

    public ChatModel() {
    }
}
