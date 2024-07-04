package com.example.androidfacebook.api;

import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostAPI {
    private String ServerurlPost;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;


    public PostAPI(String url) {
        this.setServerUrl(url);
        retrofit = new Retrofit.Builder()
                .baseUrl(this.ServerurlPost)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public WebServiceAPI getWebServiceAPI() {
        return webServiceAPI;
    }

    public void setServerUrl(String serverIp) {
        String url = "http://" + serverIp + ":8080/api/";
        this.ServerurlPost = url;
    }

    public void getAllPosts(String token, Callback<List<Post>> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<List<Post>> call = webServiceAPI.getAllPosts(token);
        call.enqueue(callback);
    }

    public void getAllComments(String token, String pid, Callback<List<Comment>> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<List<Comment>> call = webServiceAPI.getAllComments(token,pid);
        call.enqueue(callback);
    }

}
