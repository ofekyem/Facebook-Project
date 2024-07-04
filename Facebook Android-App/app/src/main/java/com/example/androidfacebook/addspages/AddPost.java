package com.example.androidfacebook.addspages;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientPost;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPost extends AppCompatActivity {
    private AppDB appDB;
    private UserDao userDao;
    private ClientUser user;
    private byte[] selectedImageByteArray;
    private ImageView selectedImageView;
    private Button btnDeletePhoto;
    private Post currentPost;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    // ActivityResultLauncher for selecting an image from the gallery
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    handleImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

    // ActivityResultLauncher for capturing an image from the camera
    private final ActivityResultLauncher<Void> mCaptureImage = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
            result -> {
                if (result != null) {
                    handleImage(result);
                }
            });
    // Handle the selected image
    private void handleImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        selectedImageByteArray = stream.toByteArray();
        selectedImageView.setImageBitmap(bitmap);
        btnDeletePhoto.setVisibility(View.VISIBLE);
    }
    public String convertByteArrayToBase64(byte[] byteArray) {
        String base64Image = "";
        if (byteArray != null && byteArray.length > 0) {
            String base64EncodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            base64Image = "data:image/jpeg;base64," + base64EncodedImage;
        }
        return base64Image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        String userId = DataHolder.getInstance().getUserLoggedInID();
        String token = DataHolder.getInstance().getToken();
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

        user = currentUser[0];
        if(user==null){
            return;
        }
        // get the views and set the listeners
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnPost = findViewById(R.id.btnPost);
        selectedImageView= findViewById(R.id.selectedImage);
        EditText TextShare = findViewById(R.id.editTextShare);
        // set the hint for the text view to include the user's name
        String hint = user.getDisplayName() + ", " + getString(R.string.add_post_edit_text);
        TextShare.setHint(hint);
        btnDeletePhoto = findViewById(R.id.btnPhotoDel);
        // set the listeners
        btnDeletePhoto.setOnClickListener(v -> {
            // Delete the photo
            selectedImageByteArray = null;
            selectedImageView.setImageBitmap(null);
            btnDeletePhoto.setVisibility(View.GONE);
        });
        // set the btnDelete listener to navigate to the pid activity
        btnDelete.setOnClickListener(v -> {
        // Navigate to addPost activity
        finish();
        });
        // set the btnPost listener to post the post
        btnPost.setOnClickListener(v -> {
            String textString = TextShare.getText().toString();
            if(textString.isEmpty()){
                showCustomToastYellow(this, "You have to write something to get it post!");
                return;
            }
            // get the current date and time
            TimeZone israelTimeZone = TimeZone.getTimeZone("Israel");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dateFormat.setTimeZone(israelTimeZone);
            String currentDateTime = dateFormat.format(new Date());
            Date date = null;
            try {
                date = dateFormat.parse(currentDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // create the post and navigate to the pid activity
            List<String> likes = new ArrayList<>();
            List<String> comments = new ArrayList<>();
            ClientPost p = new ClientPost(userId,user.getDisplayName(),user.getPhoto(),textString,null,date,0,likes,comments);
            if(selectedImageByteArray!=null){
                String image = convertByteArrayToBase64(selectedImageByteArray);
                p.setPictures(image);
            }
            UserAPI usersApi = new UserAPI(ServerIP);
            usersApi.createPost(token, p, userId, new Callback<Post>() {
                @Override
                public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                    int statusCode = response.code();
                    if(statusCode == 200){
                        currentPost = response.body();

                        showCustomToastYellow(AddPost.this, "Post created successfully");
                        new Thread(() -> appDB.postDao().insert(currentPost)).start();
                    } else if(statusCode == 300){
                        showCustomToastYellow(AddPost.this, "The url is in the BloomFilter");
                    } else{
                        showCustomToastYellow(AddPost.this, "Failed to create the post");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                    showCustomToastYellow(AddPost.this, "Invalid server call");
                }
            });

            finish();
        });

    }
    // Handle the click event of the "Add Picture" button
    public void onAddPicToPostClick(View view) {
        // Handle the click event here
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Permission has already been granted
            new AlertDialog.Builder(this)
                    .setTitle("Select Image")
                    .setItems(new String[]{"From Gallery", "From Camera"}, (dialog, which) -> {
                        if (which == 0) {
                            // From Gallery
                            mGetContent.launch("image/*");
                        } else {
                            // From Camera
                            mCaptureImage.launch(null);
                        }
                    })
                    .show();
        }
    }

}