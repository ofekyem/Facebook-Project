package com.example.androidfacebook.api;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.Post;

@Database(entities = {ClientUser.class, Post.class, Comment.class},version =4)
@TypeConverters(Convertors.class)
public abstract class AppDB extends RoomDatabase {

    public abstract PostDao postDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public void clearAllTables() {
    }

}
