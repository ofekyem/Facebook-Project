package com.example.androidfacebook.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment {
    @PrimaryKey
    @NonNull
    private String _id;
    @ColumnInfo(name = "idUserName")
    private String idUserName;
    @ColumnInfo(name = "fullname")
    private String fullname;
    @ColumnInfo(name = "icon")
    private String icon;
    @ColumnInfo(name = "idPost")
    private String idPost;
    @ColumnInfo(name = "text")
    private String text;

    /*
    this class is used to store the comment's information
     */
    public Comment(@NonNull String id, String idUserName, String fullname, String icon, String idPost, String text){
        this._id=id;
        this.idUserName=idUserName;
        this.fullname=fullname;
        this.icon=icon;
        this.idPost=idPost;
        this.text=text;
    }

    public String getIdUserName() {
        return idUserName;
    }

    public void setIdUserName(String idUserName) {
        this.idUserName = idUserName;
    }

    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
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
