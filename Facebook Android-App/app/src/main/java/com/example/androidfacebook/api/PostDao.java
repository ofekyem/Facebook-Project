package com.example.androidfacebook.api;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.androidfacebook.entities.Post;

import java.util.List;


@Dao
public interface PostDao {

    @Query("SELECT * FROM Post")
    List<Post> getAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post... post);

    @Query("DELETE FROM Post")
    void deleteAllPosts();

    @Delete
    void delete(Post post);
}
