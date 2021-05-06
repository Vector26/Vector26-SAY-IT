package com.vector.say_it;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class feedVeiw extends RecyclerView.Adapter<feedVeiw.objViewHolder> {
        JSONArray Feed;
        Context context;
        public feedVeiw(JSONArray f,Context k){
            this.Feed=f;
            this.context=k;
        }

    @NonNull
    @Override
    public feedVeiw.objViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.feed,parent,false);
        return new objViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull feedVeiw.objViewHolder holder, int position) {
        try {
            holder.post_id = (int) Feed.getJSONObject(position).getInt("id");
            holder.Content.setText(Feed.getJSONObject(position).getString("content"));
            String Date=Feed.getJSONObject(position).getString("date_posted").split("T")[0];
            holder.date.setText(Date);
            holder.id = (int) Feed.getJSONObject(position).getJSONObject("profile").getInt("id");
//            holder.User.setText("id->>"+holder.id+"  "+holder.post_id+"<<-post_id");
            holder.User.setText(Feed.getJSONObject(position).getJSONObject("profile").getJSONObject("user").getString("username"));
            String url=context.getString(R.string.BASE_URL)+Feed.getJSONObject(position).getJSONObject("profile").getString("image");
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_background)
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
        public TextView Content,User,date;
        public ImageView profile;
        public LinearLayout Card;
        public int id,post_id;
        public objViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            Content=itemView.findViewById(R.id.Content);
            User=itemView.findViewById(R.id.username);
            date=itemView.findViewById(R.id.date);
            profile=itemView.findViewById(R.id.profile_pic);
            Card=itemView.findViewById(R.id.Card);
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
            i.putExtra("id", id);
            i.putExtra("post_id", post_id);
            context.startActivity(i);
        }


    }
}
