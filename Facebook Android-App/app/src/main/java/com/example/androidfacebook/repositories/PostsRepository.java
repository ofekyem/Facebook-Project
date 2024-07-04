package com.example.androidfacebook.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidfacebook.api.PostAPI;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.entities.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsRepository {
    private PostDao postDao;
    private PostListData postListData;
    private PostAPI api;
    public PostsRepository(){
       postListData=new PostListData();
       //api = new PostAPI(postListData);

    }
    public void setPosts(List<Post> posts) {
        postListData.setValue(posts);
    }


    public class PostListData extends MutableLiveData<List<Post>> {
        public PostListData(){
            super();
            setValue(new ArrayList<>());
        }
        @Override
        protected void onActive(){
            super.onActive();
            new Thread(()-> {
                //postListData.postValue(dao.get());
            }).start();
        }

    }
    public LiveData<List<Post>> getAll() {return postListData; }
//    public void add (final Post post) {api.add(post);}
//    public void delete (final Post post) {api.delete(post)};
//    public void reload() {api.get(); }

}

