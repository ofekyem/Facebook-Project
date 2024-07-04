package com.example.androidfacebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.room.Room;

import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.CommentDao;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.login.Login;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class MainActivity extends Activity {
    private AppDB appDB;
    private UserDao userDao;
    private PostDao postDao;
    private CommentDao commentDao;
    public List<User> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        userDao = appDB.userDao();
        postDao = appDB.postDao();
        commentDao = appDB.commentDao();

        // Clear Room database when the app starts
        new Thread(() -> {
            userDao.deleteAllUsers();
            postDao.deleteAllPosts();
            if(commentDao!=null){
                commentDao.deleteAllComments();
            }

        }).start();
        // Create a new intent to start the Login activity
        userList = new ArrayList<>();
        Stack<String> s = new Stack<>();
        DataHolder.getInstance().setStackOfIDs(s);
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }
}
