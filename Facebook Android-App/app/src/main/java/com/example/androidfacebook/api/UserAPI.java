package com.example.androidfacebook.api;

import com.example.androidfacebook.entities.ClientPost;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.CommentNoID;
import com.example.androidfacebook.entities.FriendID;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.entities.UpdatePost;
import com.example.androidfacebook.entities.UpdateUser;
import com.example.androidfacebook.entities.User;
import com.example.androidfacebook.entities.UserNameAndPass;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    private String ServerurlUser;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI(String url) {
        this.setServerUrl(url);
        retrofit = new Retrofit.Builder()
                .baseUrl(this.ServerurlUser)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public WebServiceAPI getWebServiceAPI() {
        return webServiceAPI;
    }

    public void setServerUrl(String serverIp) {
        String url = "http://" + serverIp + ":8080/api/";
        this.ServerurlUser = url;
    }

    public void registerServer(User user, Callback<ResponseBody> callback){
        Call<ResponseBody> call = webServiceAPI.createUser(user);
        call.enqueue(callback);
    }
    public void loginServer(String username, String password, Callback<ResponseBody> callback){
        UserNameAndPass data = new UserNameAndPass(username, password);
        Call<ResponseBody> call = webServiceAPI.login(data);
        call.enqueue(callback);
    }

    public void getUserData(String token,String id, Callback<ClientUser> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        //UserName userStr = new UserName(username);
        Call<ClientUser> call = webServiceAPI.getUserById(token,id);
        call.enqueue(callback);
    }

    public void createPost(String token, ClientPost post, String id, Callback<Post> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<Post> call = webServiceAPI.createPost(token, post, id);
        call.enqueue(callback);
    }
    public void addOrRemoveLike(String token,String userId,String postId, Callback<Post> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<Post> call = webServiceAPI.addOrRemoveLike(token,userId,postId);
        call.enqueue(callback);

    }
    public void deletePost(String token,String userId,String postId,Callback<ResponseBody> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call= webServiceAPI.deletePost(token,userId,postId);
        call.enqueue(callback);
    }
    public void updateUser(String token, UpdateUser user, Callback<ResponseBody> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call = webServiceAPI.updateUser(token, user, user.getId());
        call.enqueue(callback);
    }

    public void deleteUser(String token, String id, Callback<ResponseBody> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call = webServiceAPI.deleteUser(token, id);
        call.enqueue(callback);
    }
    public void updatePost(String token, UpdatePost p,String postId, String userId, Callback<ResponseBody> callback) {
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call = webServiceAPI.updatePost(token,p, postId, userId);
        call.enqueue(callback);
    }
    public void getPostsByUser(String token, String id, Callback<List<Post>> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<List<Post>> call = webServiceAPI.getPostsByUser(token, id);
        call.enqueue(callback);
    }

    public void getFriends(String token, String id, Callback<List<ClientUser>> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<List<ClientUser>> call = webServiceAPI.getFriends(token, id);
        call.enqueue(callback);
    }
    public void addFriend(String token, String id, FriendID friendId, Callback<ResponseBody> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call = webServiceAPI.addFriend(token, id, friendId);
        call.enqueue(callback);
    }

    public void acceptFriendRequest(String token, String id, String friendId, Callback<ResponseBody> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call = webServiceAPI.acceptFriendRequest(token, id, friendId);
        call.enqueue(callback);
    }
    public void deleteFriendRequest(String token, String id, String friendId, Callback<ResponseBody> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call = webServiceAPI.deleteFriendRequest(token, id, friendId);
        call.enqueue(callback);
    }
    public void createComment(String token, CommentNoID comment, String userLoggedInId, Callback<Comment> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<Comment> call = webServiceAPI.createComment(token, comment, userLoggedInId,comment.getIdPost());
        call.enqueue(callback);
    }
    public void deleteComment(String token,String writerOfCommentId,String postId,String cid,Callback<ResponseBody> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<ResponseBody> call= webServiceAPI.deleteComment(token,writerOfCommentId,postId,cid);
        call.enqueue(callback);
    }
    public void updateComment(String token,String writerOfCommentId,String postId,String cid,Comment c,Callback<Comment> callback){
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }
        Call<Comment> call= webServiceAPI.updateComment(token,writerOfCommentId,postId,cid,c);
        call.enqueue(callback);
    }
}
