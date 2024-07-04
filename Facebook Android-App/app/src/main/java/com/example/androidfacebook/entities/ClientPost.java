package com.example.androidfacebook.entities;

import java.util.Date;
import java.util.List;

public class ClientPost {
    private String idUserName;
    private String fullname;
    private String icon;
    private String initialText;
    private String pictures;
    private Date time;
    private int commentsNumber;
    private List<String> likes;
    private List<String> comments;

public ClientPost(String idUserName, String fullname, String icon, String initialText, String pictures, Date time, int commentsNumber, List<String> likes, List<String> comments) {
        this.idUserName = idUserName;
        this.fullname = fullname;
        this.icon = icon;
        this.initialText = initialText;
        this.pictures = pictures;
        this.time = time;
        this.commentsNumber = commentsNumber;
        this.likes = likes;
        this.comments = comments;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(int commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

}
