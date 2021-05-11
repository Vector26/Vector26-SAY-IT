package com.vector.say_it;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

public class SearchVeiw extends RecyclerView.Adapter<SearchVeiw.objViewHolder> {
        JSONArray Feed;
        Context context;
        public SearchVeiw(JSONArray f, Context k){
            this.Feed=f;
            this.context=k;
        }

    @NonNull
    @Override
    public SearchVeiw.objViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_search,parent,false);
        return new objViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVeiw.objViewHolder holder, int position) {
        try {
            String email=Feed.getJSONObject(position).getString("email");
            holder.email.setText(email);
            holder.id = (int) Feed.getJSONObject(position).getInt("id");
//            holder.User.setText("id->>"+holder.id+"  "+holder.post_id+"<<-post_id");
            holder.User.setText(Feed.getJSONObject(position).getString("username"));
            holder.name.setText(Feed.getJSONObject(position).getString("first_name")+" "+Feed.getJSONObject(position).getString("last_name"));
            String url=context.getString(R.string.BASE_URL)+Feed.getJSONObject(position).getJSONObject("ProfileUser").getString("image");
            Glide.with(context)
                    .load(url)
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
        public TextView User,email,name;
        public ImageView profile;
        public LinearLayout Card;
        public int id;
        public objViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            User=itemView.findViewById(R.id.username);
            name=itemView.findViewById(R.id.first_name);
            email=itemView.findViewById(R.id.email);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getProfile(context);
                }
            });
        }
        public void getProfile(Context context){
            Intent i = new Intent(context, SubEvents.class);
//              Log.i("MyLogs",NotesList.size()+"");
            i.putExtra("id", id+"");
            context.startActivity(i);
        }


    }
}
