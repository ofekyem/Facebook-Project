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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfacebook.R;
import com.example.androidfacebook.api.UserAPI;
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.NotificationViewHolder> {
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
    class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconUser;
        private final TextView tvAuthor;
        private final ImageView approveButton;
        private final ImageView declineButton;

        // this is the constructor of the class
        private NotificationViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            iconUser=itemView.findViewById(R.id.iconUser);
            approveButton = itemView.findViewById(R.id.approve);
            declineButton = itemView.findViewById(R.id.decline);
        }
    }



    private final LayoutInflater mInflater;
    private List<ClientUser> notifications;
    private ClientUser userLoggedIn;
    public NotificationsListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    // this method is used to create the view holder for the notifications
    @NonNull
    @Override
    public NotificationsListAdapter.NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.notification_item,parent,false);
        return new NotificationsListAdapter.NotificationViewHolder(itemView);
    }
    // this method is used to bind the view holder with the notifications
    public void onBindViewHolder(@NonNull NotificationsListAdapter.NotificationViewHolder holder, int position) {
        if (notifications != null) {
            final ClientUser current = notifications.get(position);
            holder.tvAuthor.setText(current.getDisplayName());
            byte[] iconBytes = convertBase64ToByteArray(current.getPhoto());
            setImageViewWithBytes(holder.iconUser, iconBytes);



            // Approve Button Click Listener
            holder.approveButton.setOnClickListener(view -> {
                Context context = view.getContext();
                UserAPI userAPI = new UserAPI(ServerIP);
                String token=DataHolder.getInstance().getToken();
                userAPI.acceptFriendRequest(token, userLoggedIn.getId(), current.getId(), new Callback<ResponseBody>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        int statusCode = response.code();
                        if(statusCode == 200){
                            notifications.remove(current);
                            showCustomToastYellow(context, "added to your friends successfully");
                            notifyDataSetChanged();
                        }else{
                            showCustomToastYellow(context, "Failed to accept friend!!!!");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        showCustomToastYellow(context, "Failed to connect to server");
                    }
                });
            });

            // Decline Button Click Listener
            holder.declineButton.setOnClickListener(view -> {
                Context context = view.getContext();
                UserAPI userAPI = new UserAPI(ServerIP);
                String token=DataHolder.getInstance().getToken();
                userAPI.deleteFriendRequest(token, userLoggedIn.getId(), current.getId(), new Callback<ResponseBody>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        int statusCode = response.code();
                        if(statusCode == 200){
                            notifications.remove(current);
                            showCustomToastYellow(context, "declined friend request successfully");
                            notifyDataSetChanged();
                        }else{
                            showCustomToastYellow(context, "Failed to decline friend!!!!");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        showCustomToastYellow(context, "Failed to connect to server");
                    }
                });

            });

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setNotifications(List<ClientUser> s, ClientUser user){
        this.userLoggedIn = user;
        this.notifications = s;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if(notifications!=null){
            return notifications.size();
        }
        else{
            return 0;
        }
    }
    public List<ClientUser> getNotifications() {return notifications;}
}
