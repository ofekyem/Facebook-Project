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
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @POST("tokens")
    Call<ResponseBody> login(@Body UserNameAndPass user);
    @POST("users")
    Call<ResponseBody> createUser(@Body User user);

    @POST("users/{id}/posts")
    Call<Post> createPost(@Header("Authorization") String token, @Body ClientPost post, @Path("id") String id);

    @GET("users/{id}")
    Call<ClientUser> getUserById(@Header("Authorization") String token, @Path("id") String id);

    @PUT("users/{id}")
    Call<ResponseBody> updateUser(@Header("Authorization") String token, @Body UpdateUser user, @Path("id") String id);
    @DELETE("users/{id}")
    Call<ResponseBody> deleteUser(@Header("Authorization") String token, @Path("id") String id);
    @GET("posts")
    Call<List<Post>> getAllPosts(@Header("Authorization") String token);
    @GET("posts/{pid}")
    Call<List<Comment>> getAllComments(@Header("Authorization") String token,@Path("pid") String pid);


    @POST("users/{id}/posts/{pid}/likes")
    Call<Post> addOrRemoveLike(@Header("Authorization") String token,@Path("id") String id,@Path("pid") String pid);
    @DELETE("users/{id}/posts/{pid}")
    Call<ResponseBody> deletePost(@Header("Authorization") String token,@Path("id") String id,@Path("pid") String pid);
    @GET("users/{id}/posts")
    Call<List<Post>> getPostsByUser(@Header("Authorization") String token, @Path("id") String id);
    @PUT ("users/{id}/posts/{pid}")
    Call<ResponseBody> updatePost(@Header("Authorization") String token, @Body UpdatePost updatePost, @Path("pid") String pid, @Path("id") String id);
    @GET("users/{id}/friends")
    Call<List<ClientUser>> getFriends(@Header("Authorization") String token, @Path("id") String id);
    @POST("users/{id}/friends")
    Call<ResponseBody> addFriend(@Header("Authorization") String token, @Path("id") String id, @Body FriendID friendId);
    @PUT("users/{id}/friends/{fid}")
    Call<ResponseBody> acceptFriendRequest(@Header("Authorization") String token, @Path("id") String id, @Path("fid") String fid);
    @DELETE("users/{id}/friends/{fid}")
    Call<ResponseBody> deleteFriendRequest(@Header("Authorization") String token, @Path("id") String id, @Path("fid") String fid);
    @POST("users/{id}/posts/{pid}/comments")
    Call<Comment> createComment(@Header("Authorization") String token, @Body CommentNoID comment, @Path("id") String id,@Path("pid") String pid);
    @DELETE("users/{id}/posts/{pid}/comments/{cid}")
    Call<ResponseBody> deleteComment(@Header("Authorization") String token,@Path("id") String id,@Path("pid") String pid,@Path("cid") String cid);
    @PUT("users/{id}/posts/{pid}/comments/{cid}")
    Call<Comment> updateComment(@Header("Authorization") String token,@Path("id") String id,@Path("pid") String pid,@Path("cid") String cid,@Body Comment comment);









}
