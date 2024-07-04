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
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.UpdateUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUser extends AppCompatActivity {
    private AppDB appDB;
    private UserDao userDao;
    private ClientUser user;
    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private EditText displayName;
    private byte[] selectedImageByteArray;
    private ImageView selectedImageView;
    private Button btnDeletePhoto;
    private Button btnSubmit;
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
        setContentView(R.layout.activity_edit_user);
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
        displayName = findViewById(R.id.display_name);
        username = findViewById(R.id.username);
        username.setText(user.getUsername());
        displayName.setText(user.getDisplayName());

        // get the views and set the listeners
        Button btnCancel = findViewById(R.id.btnCancel);
        selectedImageView= findViewById(R.id.selectedImage);
        btnDeletePhoto = findViewById(R.id.btnPhotoDel);
        btnSubmit = findViewById(R.id.btnSubmit);
        // set the listeners
        btnDeletePhoto.setOnClickListener(v -> {
            // Delete the photo
            selectedImageByteArray = null;
            selectedImageView.setImageBitmap(null);
            btnDeletePhoto.setVisibility(View.GONE);
        });
        btnCancel.setOnClickListener(v -> {
            // Navigate to addPost activity
            finish();
        });
        //move to pid and delete the local data base also



        btnSubmit.setOnClickListener(v -> {

            password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        String newUsername = username.getText().toString();
        String newPassword = password.getText().toString();
        String newConfirmPassword = confirmPassword.getText().toString();
        String newDisplayName = displayName.getText().toString();

        if( !newPassword.equals("") || !newConfirmPassword.equals("")){
            if (!newPassword.equals(newConfirmPassword)) {
                showCustomToastYellow(this, "Passwords do not match");
                return;
            }
            // Check if the password meets the criteria
            String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
            if (!newPassword.matches(passwordPattern)) {
                showCustomToastYellow(this, "Password must be at least 8 characters long," +
                        " include a capital letter and a special character");
                return;
            }
        }
        if(newUsername.equals("") || newDisplayName.equals("")){
            showCustomToastYellow(this, "username and displayName are required");
            return;
        }
        UserAPI updateUserAPI = new UserAPI(ServerIP);
        String base64String = convertByteArrayToBase64(selectedImageByteArray);
        UpdateUser updateUser = new UpdateUser(currentUser[0].getId(),newUsername,newPassword,newDisplayName,base64String);
        updateUserAPI.updateUser(token, updateUser, new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                int statusCode = response.code();
                if(statusCode == 200){
                    showCustomToastYellow(EditUser.this,"User updated successfully!");
                    finish();
                }else{
                    showCustomToastYellow(EditUser.this, "Failed to update user!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                showCustomToastYellow(EditUser.this, "Invalid server call");
            }
        });

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