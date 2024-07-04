package com.example.androidfacebook.entities;

public class UpdateUser {
    private String id;
    private String username;
    private String password;
    private String displayName;
    private String photo;

    public UpdateUser(String id, String username, String password, String displayName, String photo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
