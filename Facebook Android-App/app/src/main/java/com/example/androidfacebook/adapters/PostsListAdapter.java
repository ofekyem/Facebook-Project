package com.example.androidfacebook.adapters;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.addspages.EditPost;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.PostDao;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.comments.CommentPage;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;
import com.example.androidfacebook.friends.ProfilePage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder>{
    // Set the image view with the bytes array
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

    // Create the view holder
    class PostViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final TextView tvDate;
        private final ImageView ivPic;
        private final ImageButton likeButton;
        private final ImageButton btnShare;
        private final TextView tvNumLike;
        private final TextView tvNumComment;
        private final ImageButton dotsButton;
        private final ImageView iconUser;
        private final ImageButton commentButton;
        private PostViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            tvContent=itemView.findViewById(R.id.tvContent);
            ivPic=itemView.findViewById(R.id.ivPic);
            likeButton = itemView.findViewById(R.id.likeButton);
            btnShare = itemView.findViewById(R.id.shareButton);
            iconUser=itemView.findViewById(R.id.iconUser);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvNumLike=itemView.findViewById(R.id.tvNumLike);
            tvNumComment=itemView.findViewById(R.id.tvNumComment);
            dotsButton=itemView.findViewById(R.id.dotsButton);
            commentButton=itemView.findViewById(R.id.commentButton);

        }
        // Show the popup menu when the share button is clicked
        private void showPopupShareMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.share_menu, popupMenu.getMenu());
            // Show the popup menu
            popupMenu.show();
        }
        // Show the popup menu when the option button is clicked
        @SuppressLint("NotifyDataSetChanged")
        private void showPopupOptionMenu(View view, Post current ) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.action_edit_post) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EditPost.class);
                    int indexPost = posts.indexOf(current);
                    DataHolder.getInstance().setCurrentPost(current);
                    context.startActivity(intent);
                    Post pNew = DataHolder.getInstance().getCurrentPost();
                    posts.add(indexPost,pNew);
                    notifyDataSetChanged();

                    return true;
                }

                if(id == R.id.action_delete_post){
                    Context context = view.getContext();
                    String token = DataHolder.getInstance().getToken();
                    UserAPI userAPI = new UserAPI(ServerIP);
                    userAPI.deletePost(token, user.getId(), current.getId(), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                posts.remove(current);
                                showCustomToastYellow(context, "Post deleted successfully");
                                notifyDataSetChanged();
                                new Thread(() -> {
                                    AppDB appDB = Room.databaseBuilder(context, AppDB.class, "facebookDB").build();
                                    PostDao postDao = appDB.postDao();
                                    postDao.delete(current);
                                }).start();

                            }
                            else{
                                showCustomToastYellow(context, "Can't delete this post");
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            showCustomToastYellow(context, "Something got wrong!");
                        }
                    });

                    return true;
                }
                return false;
            });
            // Show the popup menu
            popupMenu.show();
        }

    }
    private final LayoutInflater mInflater;
    private List<Post> posts;
    private ClientUser user;

    public PostsListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    @NonNull
    @Override
    // Create the view holder
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.post_item,parent,false);
        return new PostViewHolder(itemView);
    }
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position){
        // Bind the data to the view holder
        if(posts!=null){
            final Post current = posts.get(position);
            holder.tvNumComment.setText("comments: "+current.getComments().size());
            holder.tvNumLike.setText(String.valueOf(current.getLikes()));
            holder.tvAuthor.setText(current.getFullname());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
            String formattedDate = dateFormat.format(current.getTime());
            holder.tvDate.setText(formattedDate);
            holder.tvContent.setText(current.getInitialText());
            holder.tvNumLike.setText(String.valueOf(current.getLikes().size()));
            // Set the image view with the bytes array of picture
            byte[] pictureBytes = convertBase64ToByteArray(current.getPictures());
            setImageViewWithBytes(holder.ivPic, pictureBytes);
            // Set the image view with the bytes array of icon
            byte[] iconBytes = convertBase64ToByteArray(current.getIcon());
            setImageViewWithBytes(holder.iconUser,iconBytes);

            holder.iconUser.setOnClickListener(view -> {
                Context context = view.getContext();
                Stack<String> s = DataHolder.getInstance().getStackOfIDs();
                s.push(current.getIdUserName());
                DataHolder.getInstance().setStackOfIDs(s);
                Intent intent = new Intent(context, ProfilePage.class);
                context.startActivity(intent);
            });

            if(current.getLikes().contains(user.getId())){
                holder.likeButton.setImageResource(R.drawable.like_icon);
            }
            else{
                holder.likeButton.setImageResource(R.drawable.like_svgrepo_com);
            }

            holder.likeButton.setOnClickListener(view -> {
                String token = DataHolder.getInstance().getToken();
                UserAPI userAPI = new UserAPI(ServerIP);
                userAPI.addOrRemoveLike(token, user.getId(), current.getId(), new Callback<Post>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                        Context context = view.getContext();
                        if(response.isSuccessful()){
                            Post likedP = response.body();
                            int numOriginal = current.getLikes().size();
                            assert likedP != null;
                            current.setLikes(likedP.getLikes());
                            int numNew = current.getLikes().size();
                            if(numOriginal<numNew){
                                holder.likeButton.setImageResource(R.drawable.like_icon);
                            }
                            else{
                                holder.likeButton.setImageResource(R.drawable.like_svgrepo_com);
                            }
                            notifyDataSetChanged();

                        }
                        else{
                            showCustomToastYellow(context, "something wrong with this like");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                        Context context = view.getContext();
                        showCustomToastYellow(context, "something wrong with this like");

                    }
                });

            });

            holder.commentButton.setOnClickListener(view -> {
                Context context = view.getContext();
                Intent intent = new Intent(context, CommentPage.class);
                int indexPost = posts.indexOf(current);
                DataHolder.getInstance().setCurrentPost(current);
                context.startActivity(intent);
                Post up = DataHolder.getInstance().getCurrentPost();
                posts.add(indexPost,up);
            });

            // Set the onClickListener for the share button
            // Show the popup menu when the share button is clicked
            holder.btnShare.setOnClickListener(holder::showPopupShareMenu);

            if (current.getIdUserName().equals(user.getId())) {
                holder.dotsButton.setVisibility(View.VISIBLE);
                // Set the onClickListener for the option button
                holder.dotsButton.setOnClickListener(view -> {
                    // Show the popup menu when the option button is clicked
                    holder.showPopupOptionMenu(view, current);
                });
            } else {
                holder.dotsButton.setVisibility(View.GONE);
            }

        }
    }
    // Set the posts and the user
    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<Post> s, ClientUser u){
        posts = s;
        user = u;
        notifyDataSetChanged();
    }
    @Override
    // Return the number of posts
    public int getItemCount(){
        if(posts!=null){
            return posts.size();
        }
        else{
            return 0;
        }
    }
    public List<Post> getPosts() {return posts;}


}
