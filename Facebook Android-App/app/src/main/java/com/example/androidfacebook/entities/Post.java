package com.example.androidfacebook.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Post implements Serializable {
    @PrimaryKey
    @NonNull
    private String _id;
    @ColumnInfo(name = "idUserName")
    private String idUserName;
    @ColumnInfo(name = "fullname")
    private String fullname;
    @ColumnInfo(name = "icon")
    private String icon;
    @ColumnInfo(name = "initialText")
    private String initialText;
    @ColumnInfo(name = "pictures")
    private String pictures;
    @ColumnInfo(name = "time")
    private Date time;
    @ColumnInfo(name = "likes")
    private List<String> likes;
    @ColumnInfo(name = "comments")
    private List<String> comments;
    /*
    this class is used to store the post's information
     */
    // Constructor for posts with pictures
    public Post(@NonNull String id, String idUserName , String fullname, String icon, String initialText, String pictures, Date time, List<String> likes, List<String> comments) {
        this._id = id;
        this.idUserName = idUserName;
        this.fullname = fullname;
        this.icon = icon;
        this.initialText = initialText;
        this.time = time;
        this.pictures = pictures;
        this.likes = likes;
        this.comments = comments;
    }
    public void set_id(@NonNull String id){
        this._id=id;
    }
    public void setFullname(String fullname){
        this.fullname=fullname;
    }
    public void setTime(Date time){
        this.time=time;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIdUserName() {
        return idUserName;
    }
    public void setIdUserName(String idUserName) {
        this.idUserName = idUserName;
    }

    public String getId() {
        return this._id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getIcon() {
        return icon;
    }


    public String getInitialText() {
        return initialText;
    }

    public void setInitialText(String initialText) {
        this.initialText = initialText;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public Date getTime() {
        return time;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
