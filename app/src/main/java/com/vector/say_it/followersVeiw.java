package com.vector.say_it;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

public class followersVeiw extends RecyclerView.Adapter<followersVeiw.objViewHolder> {
        JSONArray Feed;
        Context context;
        int FS;
        String[] FollowState;
        public followersVeiw(JSONArray f, Context k, int FS){
            this.Feed=f;
            this.context=k;
            this.FS=FS;
            this.FollowState=new String[2];
            this.FollowState[0]="Follower";
            this.FollowState[1]="FollowedUser";
        }

    @NonNull
    @Override
    public followersVeiw.objViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_card,parent,false);
        return new objViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull followersVeiw.objViewHolder holder, int position) {
        try {
            holder.id = (int) Feed.getJSONObject(position).getJSONObject(FollowState[FS]).getInt("id");
            holder.profile_id = holder.id;
            holder.User.setText(Feed.getJSONObject(position).getJSONObject(FollowState[FS]).getJSONObject("user").getString("username"));
            String uri=context.getString(R.string.BASE_URL)+Feed.getJSONObject(position).getJSONObject(FollowState[FS]).getString("image").substring(1);
            Log.i("URI",uri);
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.default_pp_shape)
                    .circleCrop()
                    .into(holder.profile);
        }
        catch (JSONException e){
            e.getStackTrace();
        }
        }

    @Override
    public int getItemCount() {
        return Feed.length();
    }

    public void setFeed(JSONArray feed) {
        Feed = feed;
    }

    public class objViewHolder extends RecyclerView.ViewHolder{
        public TextView User;
        public ImageView profile;
        public int id,profile_id;
        public objViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            User=itemView.findViewById(R.id.username);
            profile=itemView.findViewById(R.id.profile_pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProfile(context);
                }
            });
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProfile(context);
                }
            });
            User.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProfile(context);
                }
            });
        }
        public void getProfile(Context context){
            Intent i = new Intent(context, SubEvents.class);
//              Log.i("MyLogs",NotesList.size()+"");
            i.putExtra("id", profile_id+"");
            context.startActivity(i);
        }


    }
}
