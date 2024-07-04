package com.example.androidfacebook.notification;

import static com.example.androidfacebook.login.Login.ServerIP;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.NotificationsListAdapter;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.models.UsersViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPage extends AppCompatActivity {
    private PostDao postDao;
    private String token;
    private AppDB appDB;
    private UserDao userDao;
    private ClientUser user;
    private UsersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);
        viewModel= new ViewModelProvider(this).get(UsersViewModel.class);

        String userId = DataHolder.getInstance().getUserLoggedInID();
        token = DataHolder.getInstance().getToken();
        appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                .fallbackToDestructiveMigration()
                .build();
        userDao = appDB.userDao();
        postDao = appDB.postDao();
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

        user = currentUser[0];


    }
    protected void onResume() {
        super.onResume();
        UserAPI userAPI = new UserAPI(ServerIP);
        List<String> lif = user.getFriendRequests();
        List<ClientUser> notifications = new ArrayList<>();
        AtomicInteger requestCount = new AtomicInteger(lif.size());
        for (String f : lif) {
            userAPI.getUserData(token, f, new Callback<ClientUser>() {
                @Override
                public void onResponse(@NonNull Call<ClientUser> call, @NonNull Response<ClientUser> response) {
                    if (response.isSuccessful()) {
                        ClientUser currentUser = response.body();
                        notifications.add(currentUser);
                    } else {
                        Toast.makeText(NotificationPage.this,
                                "failed to load this friend notification",
                                Toast.LENGTH_SHORT).show();
                    }
                    if (requestCount.decrementAndGet() == 0) {
                        // All requests are completed, update UI
                        final NotificationsListAdapter adapter = new NotificationsListAdapter(NotificationPage.this);
                        RecyclerView lstNotifications = findViewById(R.id.lstNotifications);
                        lstNotifications.setAdapter(adapter);
                        lstNotifications.setLayoutManager(new LinearLayoutManager(NotificationPage.this));
                        viewModel.setUsers(notifications);
                        viewModel.get().observe(NotificationPage.this, notifs -> adapter.setNotifications(notifs, user));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ClientUser> call, @NonNull Throwable t) {
                    Toast.makeText(NotificationPage.this,
                            "Invalid call from server",
                            Toast.LENGTH_SHORT).show();
                    if (requestCount.decrementAndGet() == 0) {
                        // All requests are completed, update UI
                        final NotificationsListAdapter adapter = new NotificationsListAdapter(NotificationPage.this);
                        RecyclerView lstNotifications = findViewById(R.id.lstNotifications);
                        lstNotifications.setAdapter(adapter);
                        lstNotifications.setLayoutManager(new LinearLayoutManager(NotificationPage.this));
                        viewModel.setUsers(notifications);
                        viewModel.get().observe(NotificationPage.this, notifs -> adapter.setNotifications(notifs, user));
                    }
                }
            });
        }
    }



//    @SuppressLint("MissingSuperCall")
//    @Override
//    public void onBackPressed() {
//    }

    public void onBackToPid(View view) {
        new Thread(() -> postDao.deleteAllPosts()).start();
        finish();
    }
}