package com.example.androidfacebook.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.repositories.CommentsRepository;

import java.util.List;

public class CommentsViewModel extends ViewModel {
    private CommentsRepository mRepository;
    private LiveData<List<Comment>> comments;
    public CommentsViewModel(){
        mRepository = new CommentsRepository();
        comments = mRepository.getAll();
    }
    public LiveData<List<Comment>> get() {return comments; }
    public void setComments(List<Comment> c) {
        mRepository.setComments(c);
    }
    //public void add(Comment c) {mRepository.add(c);}
    //public void delete(Comment c) {mRepository.delete(c);}
    //public void reload(){mRepository.reload();}
}
