package com.example.androidfacebook.addspages;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.UpdatePost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPost extends AppCompatActivity {

    private byte[] selectedImageByteArray;
    private ImageView selectedImageView;
    private Button btnDeletePhoto;
    private byte[] pic;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    // ActivityResultLauncher for selecting an image from the gallery
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            // The callback is called when the user has selected an image
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
    // Handle the image
    private void handleImage(Bitmap bitmap) {
        // Convert the bitmap to a byte array
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
        setContentView(R.layout.activity_edit_post);

       String token = DataHolder.getInstance().getToken();
       Post p = DataHolder.getInstance().getCurrentPost();
       pic = convertBase64ToByteArray(p.getPictures());
        // Get the views and set button click listeners

        Button btnDeleteEditPost = findViewById(R.id.btnDeleteEditPost);
        Button btnPostEditPost = findViewById(R.id.btnPostEdit);
        selectedImageView= findViewById(R.id.selectedImageEditPost);
        EditText TextShare = findViewById(R.id.editTextShareEditPost);
        setImageViewWithBytes(selectedImageView, pic);
        TextShare.setText(p.getInitialText());
        btnDeletePhoto = findViewById(R.id.btnPhotoDelEditPost);
        // Check if the post has a picture
        if(p.getPictures()==null){
            btnDeletePhoto.setVisibility(View.GONE);
        }
        else{
            btnDeletePhoto.setVisibility(View.VISIBLE);
        }
        // Set the button click listeners
        btnDeletePhoto.setOnClickListener(v -> {
            // Delete the photo
            selectedImageByteArray = null;
            selectedImageView.setImageBitmap(null);
            btnDeletePhoto.setVisibility(View.GONE);
            pic=null;
        });
        btnDeleteEditPost.setOnClickListener(v -> {
            // remain the post and go back to the previous activity
            finish();
        });
        // Set the button click listener
        btnPostEditPost.setOnClickListener(v -> {
            String textString = TextShare.getText().toString();
            // Check if the text is empty
            if(textString.isEmpty()){
                showCustomToastYellow(this, "You have to write something to get it post!");
                return;
            }
            // Update the post and go back to the previous activity

            UserAPI userApi = new UserAPI(ServerIP);
            UpdatePost up;
            if(selectedImageByteArray==null && pic==null){
                up = new UpdatePost(textString,null);

            }
            else if(selectedImageByteArray!=null){
                up= new UpdatePost(textString,convertByteArrayToBase64(selectedImageByteArray));
            }
            else{
                up= new UpdatePost(textString,convertByteArrayToBase64(pic));
            }
            userApi.updatePost(token, up, p.getId(), p.getIdUserName(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    int statusCode = response.code();
                    if(statusCode == 200){
                        showCustomToastYellow(EditPost.this,"Post updated successfully!");
                        if(selectedImageByteArray!=null){
                            p.setPictures(convertByteArrayToBase64(selectedImageByteArray));
                        }
                        p.setInitialText(textString);
                        DataHolder.getInstance().setCurrentPost(p);
                        finish();
                    } else if(statusCode == 300){
                        showCustomToastYellow(EditPost.this,"The url is on the BloomFilter!");
                    } else{
                        showCustomToastYellow(EditPost.this,"Failed to update post!");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    showCustomToastYellow(EditPost.this, "Invalid call from server");

                }
            });


        });

    }
    // Handle the permission request result
    public void onAddPicToPostClickEditPost(View view) {
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
    // Set the image view with the byte array
    public void setImageViewWithBytes(ImageView imageView, byte[] imageBytes) {
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imageView.setImageBitmap(bitmap);
        } else {
            // Set a default image or leave it empty
            imageView.setImageDrawable(null);
        }
    }
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


}