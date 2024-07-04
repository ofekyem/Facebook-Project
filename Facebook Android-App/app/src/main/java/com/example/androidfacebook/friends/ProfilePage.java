package com.example.androidfacebook.friends;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.adapters.PostsListAdapter;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.FriendID;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.models.PostsViewModel;

import java.util.List;
import java.util.Stack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePage extends AppCompatActivity {
    private AppDB appDB;
    private UserDao userDao;
    private ClientUser friendUser;
    private ImageView selectedImageView;
    private List<Post> FriendPostList;
    private PostDao postDao;
    private String token;

    private Button approveBtn;
    private Button addfriendBtn;
    private Button deleteBtn;
    private TextView waitFriend;

    private PostsViewModel viewModel;
    private ClientUser user;
    private TextView editTextName;

    private TextView editTextFriendsCount;

    public byte[] convertBase64ToByteArray(String base64Image) {
        if (base64Image != null) {
            String base64EncodedImage = null;
            if (base64Image.startsWith("data:image/jpeg;base64,")) {
                base64EncodedImage = base64Image.substring("data:image/jpeg;base64,".length());
            } else if (base64Image.startsWith("data:image/png;base64,")) {
                base64EncodedImage = base64Image.substring("data:image/png;base64,".length());
            }
            if (base64EncodedImage != null) {
                return Base64.decode(base64EncodedImage, Base64.DEFAULT);
            }
        }
        return null;
    }
    public void setImageViewWithBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or leave it empty
            imageView.setImageDrawable(null);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        viewModel= new ViewModelProvider(this).get(PostsViewModel.class);
        // Get the user that is in the pid now
        editTextName=findViewById(R.id.editText1);
        editTextFriendsCount=findViewById(R.id.editText2);
        selectedImageView = findViewById(R.id.iconUser);
        addfriendBtn = findViewById(R.id.buttonAddfriend);
        deleteBtn = findViewById(R.id.buttonDeleteFriend);
        approveBtn = findViewById(R.id.buttonApproveFriend);
        waitFriend = findViewById(R.id.friendWait);
        token = DataHolder.getInstance().getToken();
        getEverythingFromServer();

        approveBtn.setOnClickListener(view ->{
            UserAPI userAPI = new UserAPI(ServerIP);
            userAPI.acceptFriendRequest(token, user.getId(), friendUser.getId(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    int statusCode = response.code();
                    if(statusCode == 200){
                        showCustomToastYellow(ProfilePage.this, "added to your friends successfully");
                        getEverythingFromServer();

                    }else{
                        showCustomToastYellow(ProfilePage.this, "Failed to accept friend!");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    showCustomToastYellow(ProfilePage.this, "Failed to connect to server");
                }
            });
        });
        deleteBtn.setOnClickListener(view-> {
            UserAPI userAPI = new UserAPI(ServerIP);
            userAPI.deleteFriendRequest(token, user.getId(), friendUser.getId(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    int statusCode = response.code();
                    if(statusCode == 200){
                        showCustomToastYellow(ProfilePage.this, "Deleted from your friends successfully");
                        finish();

                    }else{
                        showCustomToastYellow(ProfilePage.this, "Failed to delete friend!");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    showCustomToastYellow(ProfilePage.this, "Failed to connect to server");
                }
            });
        });
        addfriendBtn.setOnClickListener(view->{
            UserAPI userAPI = new UserAPI(ServerIP);
            FriendID a = new FriendID(friendUser.getId());
            userAPI.addFriend(token, user.getId(), a, new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    int statusCode = response.code();
                    if(statusCode == 200){
                        showCustomToastYellow(ProfilePage.this, "Friend Request sent successfully!");
                        getEverythingFromServer();

                    }else{
                        showCustomToastYellow(ProfilePage.this, "Failed to request to friend!!!!");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    showCustomToastYellow(ProfilePage.this, "Failed to connect to server");
                }
            });

        });


    }



    @SuppressLint("SetTextI18n")
    protected void onResume() {
        super.onResume();
        appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        postDao = appDB.postDao();
        new Thread(() -> {
            if (postDao != null) {
                postDao.deleteAllPosts();
            }
        }).start();

        getEverythingFromServer();
    }
    public void getEverythingFromServer(){
        String userId = DataHolder.getInstance().getUserLoggedInID();
        UserAPI usersApi = new UserAPI(ServerIP);
        usersApi.getUserData(token,userId, new Callback<ClientUser>() {
            @Override
            public void onResponse(@NonNull Call<ClientUser> call, @NonNull Response<ClientUser> response) {
                if (response.isSuccessful()) {
                    ClientUser currectUser = response.body();
                    user=currectUser;
                    appDB = Room.databaseBuilder(getApplicationContext(), AppDB.class, "facebookDB")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                    userDao = appDB.userDao();
                    new Thread(() -> userDao.insert(currectUser)).start();
                    getFriendUser();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ClientUser> call, @NonNull Throwable t) {
                showCustomToastYellow(ProfilePage.this,
                        "failed to load userloggedIn");
            }
        });
    }

    public void setVisibility(){
        synchronized (this) {
            if (user != null && friendUser!=null) {
                if (user.getFriendRequests().contains(friendUser.getId())) {
                    deleteBtn.setVisibility(View.GONE);
                    addfriendBtn.setVisibility(View.GONE);
                    waitFriend.setVisibility(View.GONE);
                    approveBtn.setVisibility(View.VISIBLE);
                } else if (user.getFriendRequestsSent().contains(friendUser.getId())) {
                    approveBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    addfriendBtn.setVisibility(View.GONE);
                    waitFriend.setVisibility(View.VISIBLE);
                } else if (user.getFriendsList().contains(friendUser.getId())) {
                    approveBtn.setVisibility(View.GONE);
                    addfriendBtn.setVisibility(View.GONE);
                    waitFriend.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.VISIBLE);
                } else if(user.getId().equals(friendUser.getId())){
                    approveBtn.setVisibility(View.GONE);
                    addfriendBtn.setVisibility(View.GONE);
                    waitFriend.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                }
                else {
                    approveBtn.setVisibility(View.GONE);
                    deleteBtn.setVisibility(View.GONE);
                    waitFriend.setVisibility(View.GONE);
                    addfriendBtn.setVisibility(View.VISIBLE);
                }
            }
        }


    }

    public void getFriendUser(){

        UserAPI userAPI = new UserAPI(ServerIP);
        token = DataHolder.getInstance().getToken();
        String friendUserId = DataHolder.getInstance().getStackOfIDs().peek();


        userAPI.getUserData(token, friendUserId, new Callback<ClientUser>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ClientUser> call, @NonNull Response<ClientUser> response) {
                if (response.isSuccessful()) {
                    friendUser = response.body();
                    assert friendUser != null;
                    editTextName.setText(friendUser.getDisplayName());
                    editTextFriendsCount.setText(friendUser.getFriendsList().size()+ " Friends");
                    byte[] pictureBytes = convertBase64ToByteArray(friendUser.getPhoto());
                    setImageViewWithBytes(selectedImageView, pictureBytes);
                    setVisibility();
                    getPosts();

                }
            }

            @Override
            public void onFailure(@NonNull Call<ClientUser> call, @NonNull Throwable t) {
                showCustomToastYellow(ProfilePage.this,
                        "Invalid call from server");
            }

        });
    }

    public void getPosts(){
        UserAPI userAPI = new UserAPI(ServerIP);
        token = DataHolder.getInstance().getToken();
        String friendUserId = DataHolder.getInstance().getStackOfIDs().peek();

        userAPI.getPostsByUser(token, friendUserId, new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    FriendPostList = response.body();
                    if( FriendPostList != null){
                        for(Post p:FriendPostList){
                            new Thread(() -> postDao.insert(p)).start();
                        }
                        final PostsListAdapter adapter = new PostsListAdapter(ProfilePage.this);
                        RecyclerView lstPosts = findViewById(R.id.lstPosts);
                        lstPosts.setAdapter(adapter);
                        lstPosts.setLayoutManager(new LinearLayoutManager(ProfilePage.this));

                        viewModel.setPosts(FriendPostList);

                        viewModel.get().observe(ProfilePage.this, posts -> adapter.setPosts(posts, user));
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                showCustomToastYellow(ProfilePage.this,
                        "Invalid call from server");
            }
        });
    }

    public void goBackFromHere(View view) {
        Stack<String> s = DataHolder.getInstance().getStackOfIDs();
        s.pop();
        DataHolder.getInstance().setStackOfIDs(s);
        finish();
    }

    public void onMoveToFriendList(View view) {
        Intent moveToFriendList = new Intent(this, FriendListPage.class);
        startActivity(moveToFriendList);
    }
}