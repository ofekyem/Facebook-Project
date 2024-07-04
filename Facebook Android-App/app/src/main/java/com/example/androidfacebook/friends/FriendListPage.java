package com.example.androidfacebook.friends;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.FriendsListAdapter;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.models.UsersViewModel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendListPage extends AppCompatActivity {
    private String token;
    private AppDB appDB;
    private UserDao userDao;
    private TextView nameOfFriendTextView;
    private ClientUser userLoggedIn;
    private ClientUser viewedFriendUser;
    private UsersViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list_page);
        viewModel= new ViewModelProvider(this).get(UsersViewModel.class);
        nameOfFriendTextView = findViewById(R.id.nameOfFriend);

        String userId = DataHolder.getInstance().getUserLoggedInID();
        token = DataHolder.getInstance().getToken();
        appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = appDB.userDao();
        final ClientUser[] currentUser = new ClientUser[1];
        CountDownLatch latch = new CountDownLatch(1); // Create a CountDownLatch with a count of 1

        new Thread(() -> {
            currentUser[0] = appDB.userDao().getUserById(userId);
            latch.countDown(); // Decrease the count
        }).start();

        try {
            latch.await(); // Main thread waits here until count reaches zero
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userLoggedIn = currentUser[0];
        Button goBack = findViewById(R.id.goBackButton);
        goBack.setOnClickListener(view -> finish());
    }
    @SuppressLint("SetTextI18n")
    protected void onResume() {
        super.onResume();
        getUser();
    }
    public void getUser(){
        UserAPI userAPI = new UserAPI(ServerIP);
        token = DataHolder.getInstance().getToken();
        String viewedFriendUserId = DataHolder.getInstance().getStackOfIDs().peek();


        userAPI.getUserData(token, viewedFriendUserId, new Callback<ClientUser>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ClientUser> call, @NonNull Response<ClientUser> response) {
                if (response.isSuccessful()) {
                    viewedFriendUser = response.body();
                    assert viewedFriendUser != null;
                    nameOfFriendTextView.setText(viewedFriendUser.getDisplayName() + "'s Friends");
                    userAPI.getFriends(token, viewedFriendUserId, new Callback<List<ClientUser>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<ClientUser>> call, @NonNull Response<List<ClientUser>> response) {
                            int statusCode = response.code();
                            if(statusCode == 200){
                                List<ClientUser> users =response.body();
                                final FriendsListAdapter adapter = new FriendsListAdapter(FriendListPage.this);
                                RecyclerView lstFriends = findViewById(R.id.lstFriends);
                                lstFriends.setAdapter(adapter);
                                lstFriends.setLayoutManager(new LinearLayoutManager(FriendListPage.this));
                                viewModel.setUsers(users);
                                viewModel.get().observe(FriendListPage.this, frie -> adapter.setFriends(frie,viewedFriendUser, userLoggedIn));


                            }
                            else{
                                showCustomToastYellow(FriendListPage.this, "Failed to get user friends list");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<ClientUser>> call, @NonNull Throwable t) {
                            showCustomToastYellow(FriendListPage.this, "Invalid call to server");
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClientUser> call, @NonNull Throwable t) {
                showCustomToastYellow(FriendListPage.this, "Invalid call to server");
            }

        });
    }
}