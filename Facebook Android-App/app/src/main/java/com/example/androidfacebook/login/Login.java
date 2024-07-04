package com.example.androidfacebook.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.api.UserDao;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.pid.Pid;
import com.example.androidfacebook.signup.SignUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    This class is the main class for the login page.
 */
public class Login extends AppCompatActivity {
    public static String ServerIP = "10.0.2.2";
    private AppDB appDB;
    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get what the user has entered in the email and password fields
        EditText emailOrPhoneEditText = findViewById(R.id.editText);
        EditText passwordEditText = findViewById(R.id.editText2);
        Button btnLogin = findViewById(R.id.button);
        Button btnSignUp = findViewById(R.id.button2);
        // when the user clicks the login button
        btnLogin.setOnClickListener(v -> {
            String emailOrPhone = emailOrPhoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if(emailOrPhone.isEmpty() || password.isEmpty()){
                showCustomToastYellow(this,"You must fill all the boxes");
                return;
            }
            UserAPI usersApi = new UserAPI(ServerIP);
            Intent intent2 = new Intent(this, Pid.class);
            usersApi.loginServer(emailOrPhone, password, new Callback<ResponseBody>() {

                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String status = String.valueOf(response.code());
                        String userID="";
                        String token="";
                        try {
                            assert response.body() != null;
                            String responseBody = response.body().string();
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            userID = jsonResponse.getString("userId");
                            token = jsonResponse.getString("token");
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                            if (status.equals("200")) {

                                String finalToken = token;
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
                                                new Thread(() -> userDao.insert(currectUser)).start();

                                            DataHolder.getInstance().setToken(finalToken);
                                            assert currectUser != null;
                                            DataHolder.getInstance().setUserLoggedInID(currectUser.getId());
                                            startActivity(intent2);

                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ClientUser> call, @NonNull Throwable t) {
                                        showCustomToastYellow(Login.this,"failed to load this user");
                                    }
                                });

                            }
                            else {
                                showCustomToastYellow(Login.this,"Login request failed 404");
                            }

                    } else {
                        showCustomToastYellow(Login.this,"invalid username or password");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    showCustomToastYellow(Login.this,"Invalid server call!");
                }
            });

        });
        // when the user clicks the sign up button
        btnSignUp.setOnClickListener(v -> {
            // go to the sign up page.
            Intent intent1 = new Intent(this, SignUp.class);
            startActivity(intent1);
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clear Room database when the app is closing
        new Thread(() -> {

            if (userDao != null) {
                userDao.deleteAllUsers();
            }
        }).start();
    }

    public static void showCustomToastYellow(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_warning, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP, 0, 32);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
    }
}