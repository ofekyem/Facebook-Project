package com.example.androidfacebook.entities;

public class UpdatePost {
    private String initialText;
    private String pictures;

    public UpdatePost(String initialText, String pictures) {
        this.initialText = initialText;
        this.pictures = pictures;
    }

    public String getInitialText() {
        return initialText;
    }

    public void setInitialText(String initialText) {
        this.initialText = initialText;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }
}
