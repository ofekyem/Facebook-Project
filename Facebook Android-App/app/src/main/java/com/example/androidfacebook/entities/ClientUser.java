package com.example.androidfacebook.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class ClientUser implements Serializable {
    @PrimaryKey
    @NonNull
    private String _id;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "displayName")
    private String displayName;
    @ColumnInfo(name = "photo")
    private String photo;
    @ColumnInfo(name = "postList")
    private List<String> postList;
    @ColumnInfo(name = "friendsList")
    private List<String> friendsList;
    @ColumnInfo(name = "friendRequests")
    private List<String> friendRequests;
    @ColumnInfo(name = "friendRequestsSent")
    private List<String> friendRequestsSent;
    @ColumnInfo(name = "likes")
    private List<String> likes;
    @ColumnInfo(name = "comments")
    private List<String> comments;


    public List<String> getPostList() {
        return postList;
    }

    public void setPostList(List<String> postList) {
        this.postList = postList;
    }

    public List<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List<String> getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public void setFriendRequestsSent(List<String> friendRequestsSent) {
        this.friendRequestsSent = friendRequestsSent;
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



    /*
    this class is used to store the user's information
     */
    public ClientUser(@NonNull String _id, String username, String displayName, String photo,
                      List<String> postList, List<String> friendsList, List<String> friendRequests,
                      List<String>  friendRequestsSent, List<String>  likes, List<String>  comments){
        this._id=_id;
        this.username = username;
        this.displayName = displayName;
        this.photo = photo;
        this.postList = postList;
        this.friendsList = friendsList;
        this.friendRequests = friendRequests;
        this.friendRequestsSent = friendRequestsSent;
        this.likes = likes;
        this.comments = comments;
    }


    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}

