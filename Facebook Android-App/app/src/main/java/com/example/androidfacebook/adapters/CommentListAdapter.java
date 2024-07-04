package com.example.androidfacebook.adapters;

import static com.example.androidfacebook.login.Login.ServerIP;
import static com.example.androidfacebook.login.Login.showCustomToastYellow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.AppDB;
import com.example.androidfacebook.api.CommentDao;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.Comment;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.entities.Post;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
this class is the adapter for the comments list
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {
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
    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton CommentButtonOption;
        private final ImageView iconUser;
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final TextView editCommentTextView;
        private final Button btnSaveComment;
        private final Button btnCancelEdit;

        // this is the constructor of the class
        private CommentViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            tvContent=itemView.findViewById(R.id.tvContent);
            iconUser=itemView.findViewById(R.id.iconUser);
            CommentButtonOption=itemView.findViewById(R.id.CommentButtonOption);
            editCommentTextView=itemView.findViewById(R.id.editCommentTextView);
            btnSaveComment = itemView.findViewById(R.id.btnSaveComment);
            btnCancelEdit = itemView.findViewById(R.id.btnCancelEdit);
        }

        // this method is used to show the popup menu for the comments
        private void showPopupOptionMenu(View view, Comment current ) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.getMenuInflater().inflate(R.menu.option_menu_comments, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.action_edit_comment) {
                    if (userLoggedIn.getId().equals(current.getIdUserName())) {
                        if (tvContent.getVisibility() == View.VISIBLE) {
                            tvContent.setVisibility(View.GONE);
                            editCommentTextView.setVisibility(View.VISIBLE);
                            editCommentTextView.setText(tvContent.getText());
                            btnCancelEdit.setVisibility(View.VISIBLE);
                            btnSaveComment.setVisibility(View.VISIBLE);
                        } else {
                            tvContent.setVisibility(View.VISIBLE);
                            editCommentTextView.setVisibility(View.GONE);
                        }
                        return true;
                    } else {
                        //Toast.makeText(view.getContext(), "You cannot edit this comment because you are the owner of the post.", Toast.LENGTH_SHORT).show();
                        showCustomToastYellow(view.getContext(),"You cannot edit this comment because you are not the owner of this comment!.");
                        return true;
                    }
                }


                if(id == R.id.action_delete_comment){

                    Context context = view.getContext();
                    String token = DataHolder.getInstance().getToken();
                    UserAPI userAPI = new UserAPI(ServerIP);
                    userAPI.deleteComment(token, current.getIdUserName(), current.getIdPost(), current.get_id(), new Callback<ResponseBody>() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                comments.remove(current);
                                showCustomToastYellow(context,"Comment deleted successfully");
                                notifyDataSetChanged();
                                new Thread(() -> {
                                    AppDB appDB = Room.databaseBuilder(context, AppDB.class, "facebookDB").build();
                                    CommentDao commentDao = appDB.commentDao();
                                    commentDao.delete(current);
                                }).start();
                                List<String> lc = currentPost.getComments();
                                lc.remove(current.get_id());
                                currentPost.setComments(lc);
                                DataHolder.getInstance().setCurrentPost(currentPost);

                            }
                            else{
                                showCustomToastYellow(context,"Can't delete this comment");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            showCustomToastYellow(context,"Invalid call from Server");
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
    private List<Comment> comments;
    private ClientUser userLoggedIn;
    private ClientUser postUser; //post writer
    private Post currentPost;

    public CommentListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    // this method is used to create the view holder for the comments
    @NonNull
    @Override
    public CommentListAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.comment_item,parent,false);
        return new CommentListAdapter.CommentViewHolder(itemView);
    }
    // this method is used to bind the view holder with the comments
    public void onBindViewHolder(@NonNull CommentListAdapter.CommentViewHolder holder, int position) {
        if (comments != null) {
            final Comment current = comments.get(position);
            holder.tvAuthor.setText(current.getFullname());
            holder.tvContent.setText(current.getText());
            byte[] iconBytes = convertBase64ToByteArray(current.getIcon());
            setImageViewWithBytes(holder.iconUser, iconBytes);
            if((userLoggedIn.getId().equals(current.getIdUserName()))||(userLoggedIn.getId().equals(currentPost.getIdUserName()))){
                holder.CommentButtonOption.setVisibility(View.VISIBLE);
            }
            else{
                holder.CommentButtonOption.setVisibility(View.GONE);
            }

            holder.CommentButtonOption.setOnClickListener(view -> holder.showPopupOptionMenu(view, current));

            holder.btnSaveComment.setOnClickListener(v -> {
                String editedComment = holder.editCommentTextView.getText().toString();
                if(editedComment.isEmpty()){
                    showCustomToastYellow(v.getContext(), "Comment can't be blank!");
                    return;
                }
                // Save the edited comment
                Comment newC = new Comment(current.get_id(),current.getIdUserName(),current.getFullname(),current.getIcon(),current.getIdPost(),editedComment);
                Context context = v.getContext();
                String token = DataHolder.getInstance().getToken();
                UserAPI userAPI = new UserAPI(ServerIP);

                userAPI.updateComment(token, current.getIdUserName(), current.getIdPost(), current.get_id(), newC, new Callback<Comment>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<Comment> call, @NonNull Response<Comment> response) {
                        int statusCode = response.code();
                        if(statusCode == 200){
                            List<String> lc = currentPost.getComments();
                            int indexC = lc.indexOf(current.get_id());
                            current.setText(editedComment);
                            comments.set(comments.indexOf(current),current);
                            holder.tvContent.setText(current.getText());
                            lc.add(indexC,current.get_id());
                            currentPost.setComments(lc);
                            DataHolder.getInstance().setCurrentPost(currentPost);
                            new Thread(() -> {
                                AppDB appDB = Room.databaseBuilder(context, AppDB.class, "facebookDB").build();
                                CommentDao commentDao = appDB.commentDao();
                                commentDao.insert(current);
                            }).start();
                            showCustomToastYellow(context,"comment updated!");
                            notifyDataSetChanged();
                        }else{
                            //Toast.makeText(context, "Failed to update comment!!!!", Toast.LENGTH_SHORT).show();
                            showCustomToastYellow(context,"Failed to update comment");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Comment> call, @NonNull Throwable t) {
                        showCustomToastYellow(context,"Invalid call from Server");
                    }
                });

                holder.btnCancelEdit.setVisibility(View.GONE);
                holder.btnSaveComment.setVisibility(View.GONE);
                holder.editCommentTextView.setVisibility(View.GONE);
                holder.tvContent.setVisibility(View.VISIBLE);
            });

            holder.btnCancelEdit.setOnClickListener(v -> {
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.editCommentTextView.setVisibility(View.GONE);
                holder.editCommentTextView.setText(current.getText());
                holder.btnCancelEdit.setVisibility(View.GONE);
                holder.btnSaveComment.setVisibility(View.GONE);
            });

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setComments(List<Comment> s, Post currentPost, ClientUser postUser, ClientUser userLoggedIn){
        this.postUser = postUser;
        this.userLoggedIn=userLoggedIn;
        this.currentPost = currentPost;
        comments = s;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if(comments!=null){
            return comments.size();
        }
        else{
            return 0;
        }
    }
    public List<Comment> getComments() {return comments;}

}