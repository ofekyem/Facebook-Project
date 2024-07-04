package com.example.androidfacebook.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.entities.ClientUser;

import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
    private UsersRepository.UsersListData usersListData;
    private UserAPI api;
    public UsersRepository(){
        usersListData=new UsersRepository.UsersListData();
        //api = new PostAPI(postListData);

    }
    public void setUsers(List<ClientUser> users) {
        usersListData.setValue(users);
    }


    public class UsersListData extends MutableLiveData<List<ClientUser>> {
        public UsersListData(){
            super();
            setValue(new ArrayList<>());
        }
        @Override
        protected void onActive(){
            super.onActive();

        }

    }
    public LiveData<List<ClientUser>> getAll() {return usersListData; }


}
