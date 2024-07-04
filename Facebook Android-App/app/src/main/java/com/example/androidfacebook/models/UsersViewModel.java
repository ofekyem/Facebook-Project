package com.example.androidfacebook.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.repositories.UsersRepository;

import java.util.List;

public class UsersViewModel extends ViewModel {
    private UsersRepository mRepository;
    private LiveData<List<ClientUser>> users;
    public UsersViewModel(){
        mRepository = new UsersRepository();
        users = mRepository.getAll();
    }
    public LiveData<List<ClientUser>> get() {return users; }
    public void setUsers(List<ClientUser> u) {
        mRepository.setUsers(u);
    }

}
