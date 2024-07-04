package com.example.androidfacebook.entities;


public class CommentNoID {

    private String idUserName;
    private String fullname;
    private String icon;
    private String idPost;
    private String text;

    public CommentNoID(String idUserName, String fullname, String icon, String idPost, String text) {
        this.idUserName = idUserName;
        this.fullname = fullname;
        this.icon = icon;
        this.idPost = idPost;
        this.text = text;
    }

    public String getIdUserName() {
        return idUserName;
    }

    public void setIdUserName(String idUserName) {
        this.idUserName = idUserName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
