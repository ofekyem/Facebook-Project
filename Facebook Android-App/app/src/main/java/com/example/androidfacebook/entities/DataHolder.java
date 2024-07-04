package com.example.androidfacebook.entities;

import java.util.Stack;

/*
This class is used to store some of the data
 */
public class DataHolder {
    private static DataHolder instance;
    private Stack<String> stackOfIDs;
    private Post currentPost;
    private String token;
    private String userLoggedInID;

    private DataHolder() {
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserLoggedInID() {
        return userLoggedInID;
    }

    public void setUserLoggedInID(String userLoggedInID) {
        this.userLoggedInID = userLoggedInID;
    }

    public Post getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(Post currentPost) {
        this.currentPost = currentPost;
    }

    public Stack<String> getStackOfIDs() {
        return stackOfIDs;
    }

    public void setStackOfIDs(Stack<String> stackOfIDs) {
        this.stackOfIDs = stackOfIDs;
    }
}