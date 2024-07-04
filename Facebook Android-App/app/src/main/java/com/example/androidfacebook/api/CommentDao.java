package com.example.androidfacebook.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.Post;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comment")
    List<Comment> getAllComments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment... comment);

    @Query("DELETE FROM Comment")
    void deleteAllComments();

    @Delete
    void delete(Comment comment);
}
