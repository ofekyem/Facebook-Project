package com.example.androidfacebook.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.androidfacebook.entities.ClientUser;
import com.example.androidfacebook.entities.DataHolder;
import com.example.androidfacebook.friends.ProfilePage;

import java.util.List;
import java.util.Stack;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendViewHolder> {
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
    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final ImageView iconUser;
        private final TextView tvAuthor;

        // this is the constructor of the class
        private FriendViewHolder(View itemView){
            super(itemView);
            tvAuthor=itemView.findViewById(R.id.tvAuthor);
            iconUser=itemView.findViewById(R.id.iconUser);
        }
    }

    private final LayoutInflater mInflater;
    private List<ClientUser> friends;
    private ClientUser userLoggedIn;

    private ClientUser viewedFriend;
    public FriendsListAdapter(Context context){mInflater=LayoutInflater.from(context);}

    // this method is used to create the view holder for the friends
    @NonNull
    @Override
    public FriendsListAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.friend_item,parent,false);
        return new FriendsListAdapter.FriendViewHolder(itemView);
    }
    // this method is used to bind the view holder with the friends
    public void onBindViewHolder(@NonNull FriendsListAdapter.FriendViewHolder holder, int position) {
        if (friends != null) {
            final ClientUser current = friends.get(position);
            holder.tvAuthor.setText(current.getDisplayName());
            byte[] iconBytes = convertBase64ToByteArray(current.getPhoto());
            setImageViewWithBytes(holder.iconUser, iconBytes);
            holder.iconUser.setOnClickListener(view -> {
                Context context = view.getContext();
                Stack<String> s = DataHolder.getInstance().getStackOfIDs();
                s.push(current.getId());
                DataHolder.getInstance().setStackOfIDs(s);
                Intent intent = new Intent(context, ProfilePage.class);
                context.startActivity(intent);
            });

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setFriends(List<ClientUser> s, ClientUser viewedFriend, ClientUser userLoggedIn){
        this.userLoggedIn = userLoggedIn;
        this.viewedFriend = viewedFriend;
        this.friends = s;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount(){
        if(friends!=null){
            return friends.size();
        }
        else{
            return 0;
        }
    }
    public List<ClientUser> getFriends() {return friends;}
}

