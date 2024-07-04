package com.example.androidfacebook.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidfacebook.api.CommentDao;
import com.example.androidfacebook.api.PostAPI;
import com.example.androidfacebook.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsRepository {
    private CommentDao commentDao;
    private CommentsRepository.CommentListData commentListData;
    private PostAPI api;
    public CommentsRepository(){
        commentListData=new CommentListData();
        //api = new PostAPI(postListData);

    }
    public void setComments(List<Comment> comments) {
        commentListData.setValue(comments);
    }


    public class CommentListData extends MutableLiveData<List<Comment>> {
        public CommentListData(){
            super();
            setValue(new ArrayList<>());
        }
        @Override
        protected void onActive(){
            super.onActive();
            new Thread(()-> {
                //commentListData.commentValue(dao.get());
            }).start();
        }

    }
    public LiveData<List<Comment>> getAll() {return commentListData; }
//    public void add (final Comment c) {api.add(c);}
//    public void delete (final Comment c) {api.delete(c)};
//    public void reload() {api.get(); }
}
