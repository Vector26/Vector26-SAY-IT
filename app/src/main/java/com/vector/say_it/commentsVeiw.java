package com.vector.say_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class commentsVeiw extends RecyclerView.Adapter<commentsVeiw.objViewHolder> {
        JSONArray Feed;
        Context context;
        single_post frag;
        public commentsVeiw(JSONArray f, Context k,single_post t){
            this.Feed=f;
            this.context=k;
            this.frag=t;
        }

    @NonNull
    @Override
    public commentsVeiw.objViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comments,parent,false);
        return new objViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull commentsVeiw.objViewHolder holder, int position) {
        try {
            holder.id = (int) Feed.getJSONObject(position).getInt("id");
            holder.isOwned=Feed.getJSONObject(position).getInt("isOwned");
//            holder.User.setText("id->>"+holder.id+"  "+holder.post_id+"<<-post_id");
            holder.User.setText(Feed.getJSONObject(position).getJSONObject("commenter").getJSONObject("user").getString("username"));
            String uri=Feed.getJSONObject(position).getJSONObject("commenter").getString("image");
            Log.i("URI",uri);
            holder.comment.setText(Feed.getJSONObject(position).getString("comment"));
            if(holder.isOwned==0){
                holder.Delete.setVisibility(View.GONE);
            }
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
        public TextView User,comment;
        int isOwned;
        public ImageView profile;
        public int id;
        public Button Delete;
        public objViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            User=itemView.findViewById(R.id.comment_username);
            comment=itemView.findViewById(R.id.the_comment);
            Delete=itemView.findViewById(R.id.delete_comment);
            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    frag.deleteComment(id);
                }
            });
            profile=itemView.findViewById(R.id.comment_profile_pic);
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
            Intent i = new Intent(context, MainActivity2.class);
//              Log.i("MyLogs",NotesList.size()+"");
            i.putExtra("id", id+"");
            context.startActivity(i);
        }


    }
}
