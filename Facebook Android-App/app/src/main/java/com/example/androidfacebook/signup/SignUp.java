package com.example.androidfacebook.signup;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {
    private EditText username;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private EditText password;
    private EditText confirmPassword;
    private EditText displayName;
    private byte[] selectedImageByteArray; // Variable to hold the selected image's byte array
    // Declare two ActivityResultLaunchers for picking from gallery and capturing from camera
    // Declare an ActivityResultLauncher for picking an image from gallery
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    handleImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    // Declare an ActivityResultLauncher for capturing an image from camera
    private final ActivityResultLauncher<Void> mCaptureImage = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
            result -> {
                if (result != null) {
                    handleImage(result);
                }
            });
    // Method to handle the image
    private void handleImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        selectedImageByteArray = stream.toByteArray();
    }
    public String convertByteArrayToBase64(byte[] byteArray) {
        String base64Image = "";
        if (byteArray != null && byteArray.length > 0) {
            String base64EncodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            base64Image = "data:image/jpeg;base64," + base64EncodedImage;
        }
        return base64Image;
    }




    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.textView3);
        password = findViewById(R.id.editTextTextPassword2);
        confirmPassword = findViewById(R.id.editTextTextPassword4);
        displayName = findViewById(R.id.editTextText2);
        Button btnGoBack = findViewById(R.id.btnGoBack);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        Button btnUploadImage = findViewById(R.id.btnSelectPhoto);

        // Go back to the login page
        btnGoBack.setOnClickListener(v -> finish());

        btnUploadImage.setOnClickListener(v -> {
            // Check for camera permission
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
        });

        // Sign up the user
        btnSignUp.setOnClickListener(v -> {
            String usernameStr = username.getText().toString();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();
            String displayNameStr = displayName.getText().toString();
            // Check if any of the fields are empty
            if (usernameStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty() || displayNameStr.isEmpty() || selectedImageByteArray == null) {
                // show error message.
                showCustomToastYellow(this, "All fields are required");
                return;
            }
            // Check if the password meets the criteria
            String passwordPattern = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
            if (!passwordStr.matches(passwordPattern)) {
                showCustomToastYellow(this,"Password must be at least 8 characters long," +
                        " include a capital letter and a special character");
                return;
            }
            // Check if the passwords and confirm password match
            if (!passwordStr.equals(confirmPasswordStr)) {
                showCustomToastYellow(this, "Passwords do not match");
                return;
            }
            UserAPI reigsterUserAPI = new UserAPI(ServerIP);
            String base64String = convertByteArrayToBase64(selectedImageByteArray);
            User request =  new User(usernameStr,passwordStr,displayNameStr,base64String);
            reigsterUserAPI.registerServer(request, new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    int statusCode = response.code();
                    if (statusCode == 200) {
                        showCustomToastYellow(SignUp.this,"User Registered successfully!");
                        finish();
                    }
                    else {
                        showCustomToastYellow(SignUp.this,"User already exist");

                    }
                }
                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    showCustomToastYellow(SignUp.this,"Failed to connect to server");
                }
            });

        });
    }

}