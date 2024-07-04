package com.example.androidfacebook.pid;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.addspages.AddPost;
import com.example.androidfacebook.addspages.EditUser;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.CommentDao;
import com.example.androidfacebook.api.PostAPI;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.friends.ProfilePage;
import com.example.androidfacebook.models.PostsViewModel;
import com.example.androidfacebook.notification.NotificationPage;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.CountDownLatch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    Pid class is the main class for the user to see posts and add new posts
 */
public class Pid extends AppCompatActivity {
    private AppDB appDB;
    private UserDao userDao;
    private ClientUser user;
    private List<Post> postList;
    private PostDao postDao;
    private CommentDao commentDao;
    private String token;
    private TextView redCircle;

    private PostsViewModel viewModel;

    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pid);
        redCircle=findViewById(R.id.notificationBadge);
        viewModel= new ViewModelProvider(this).get(PostsViewModel.class);

        // Get the user that is in the pid now

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

        user = currentUser[0]; // now user is the userLogged in
        if(user!=null){
            if(!user.getFriendRequests().isEmpty()){
                redCircle.setText(String.valueOf(user.getFriendRequests().size()));
                redCircle.setVisibility(View.VISIBLE);
            }
            else{
                redCircle.setVisibility(View.GONE);
            }
        }


        Button btnAddPost = findViewById(R.id.btnAddPost);
        // When the user clicks on the add post button,
        // the user will be redirected to the add post page
        btnAddPost.setOnClickListener(v -> {
            Intent i = new Intent(this, AddPost.class);
            startActivity(i);
        });

        ImageButton menuIcon = findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(this::showPopupMenu);


    }
    protected void onResume() {
        super.onResume();
        postDao = appDB.postDao();
        commentDao = appDB.commentDao();
        token = DataHolder.getInstance().getToken();
        PostAPI postsApi = new PostAPI(ServerIP);
        new Thread(() -> {
            if(commentDao!=null){
                commentDao.deleteAllComments();
            }
            postDao.deleteAllPosts();
        }).start();
        postsApi.getAllPosts(token, new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                postList = response.body();
                postDao = appDB.postDao();
                for(Post p:postList){
                    new Thread(() -> postDao.insert(p)).start();
                }
                final PostsListAdapter adapter = new PostsListAdapter(Pid.this);
                RecyclerView lstPosts = findViewById(R.id.lstPosts);
                lstPosts.setAdapter(adapter);
                lstPosts.setLayoutManager(new LinearLayoutManager(Pid.this));
                viewModel.setPosts(postList);
                viewModel.get().observe(Pid.this, posts -> adapter.setPosts(posts, user));
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<List<Post>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
        UserAPI usersApi = new UserAPI(ServerIP);
        String userID = DataHolder.getInstance().getUserLoggedInID();

        usersApi.getUserData(token,userID, new Callback<ClientUser>() {
            @Override
            public void onResponse(@NonNull Call<ClientUser> call, @NonNull Response<ClientUser> response) {
                if(response.isSuccessful()){
                    ClientUser currectUser = response.body();
                    appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                    userDao= appDB.userDao();
                    new Thread(() -> {
                        userDao.insert(currectUser);

                    }).start();
                    user=currectUser;
                    assert user != null;
                    if(!user.getFriendRequests().isEmpty()){
                        redCircle.setText(String.valueOf(user.getFriendRequests().size()));
                        redCircle.setVisibility(View.VISIBLE);
                    }
                    else{
                        redCircle.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<ClientUser> call, @NonNull Throwable t) {

                showCustomToastYellow(Pid.this,
                        "failed to load this page");
            }
        });

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
    public void onNotificationClick(View view){
        Intent a = new Intent(this, NotificationPage.class);
        startActivity(a);
    }

    // Show the popup menu
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.navi_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_darkMode) {
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                return true;
            }
            if (id == R.id.edit_user) {
                Intent intent = new Intent(this, EditUser.class);
                startActivity(intent);
                return true;
            }
            if (id == R.id.action_logOut) {
                new Thread(() -> {
                    if (userDao != null) {
                        userDao.deleteAllUsers();
                    }
                    if(commentDao!=null){
                        commentDao.deleteAllComments();
                    }
                    postDao.deleteAllPosts();
                }).start();
                finish();
                return true;
            }
            if(id == R.id.action_delUser){
                UserAPI deleteUserAPI = new UserAPI(ServerIP);
                deleteUserAPI.deleteUser(token, user.getId(), new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        int statusCode = response.code();
                        if(statusCode == 200){
                            showCustomToastYellow(Pid.this, "User deleted successfully");
                            new Thread(() -> {
                                if (userDao != null) {
                                    userDao.deleteAllUsers();
                                }
                                if(commentDao!=null){
                                    commentDao.deleteAllComments();
                                }
                                postDao.deleteAllPosts();
                            }).start();
                            finish();
                        }else{
                            //Toast.makeText(Pid.this, "Failed to delete user!!!!", Toast.LENGTH_SHORT).show();
                            showCustomToastYellow(Pid.this, "Failed to delete user!");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        //Toast.makeText(Pid.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                        showCustomToastYellow(Pid.this, "Invalid server call");
                    }
                });
                return true;

            }
            return false;
        });
        popupMenu.show();
    }

    public void goToMyProfile(View view) {
        Stack<String> s = DataHolder.getInstance().getStackOfIDs();
        s.push(user.getId());
        DataHolder.getInstance().setStackOfIDs(s);
        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }
}