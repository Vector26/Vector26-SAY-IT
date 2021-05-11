package com.vector.say_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class feedVeiw extends RecyclerView.Adapter<feedVeiw.objViewHolder> {
        JSONArray Feed;
        Context context;
        SharedPreferences sharedPreferences;
        String feedType;
        ProfileFragment profileFragment;
        public feedVeiw(JSONArray f,Context k,SharedPreferences s,String FeedType,ProfileFragment pf){
            this.Feed=f;
            this.profileFragment=pf;
            this.sharedPreferences=s;
            this.context=k;
            this.feedType=FeedType;
        }
    public feedVeiw(JSONArray f,Context k,SharedPreferences s,String FeedType){
        this.Feed=f;
        this.sharedPreferences=s;
        this.context=k;
        this.feedType=FeedType;
        this.profileFragment=null;
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
            holder.likes.setText(Feed.getJSONObject(position).getInt("likes")+"");
//            holder.likes.setText("Hello");
            holder.isLiked=Feed.getJSONObject(position).getInt("isLiked");
            if(holder.isLiked==1){
                holder.alt=0;
                holder.like.setBackground(context.getDrawable(R.drawable.ic_heart));
            }
            else{holder.alt=1;}
            holder.drawables[2]=holder.like.getBackground();
            holder.comments.setText(Feed.getJSONObject(position).getJSONArray("comments").length()+"");
            holder.Content.setText(Feed.getJSONObject(position).getString("content"));
            String[] Date=Feed.getJSONObject(position).getString("date_posted").split("T");
            String[] Time=Date[1].split(":");
            holder.date.setText(Time[0]+":"+Time[1]+"   "+Date[0]);
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, v);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.post_options);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_post:
                                    //handle menu1 click
                                    profileFragment.delete(holder.post_id);
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.like(context,Feed,position);
                    YoYo.with(Techniques.BounceIn)
                            .duration(400)
                            .playOn(v);
                }
            });
            holder.id = (int) Feed.getJSONObject(position).getJSONObject("profile").getInt("id");
//            holder.User.setText("id->>"+holder.id+"  "+holder.post_id+"<<-post_id");
            holder.User.setText(Feed.getJSONObject(position).getJSONObject("profile").getJSONObject("user").getString("username"));
            String uri=Feed.getJSONObject(position).getJSONObject("profile").getString("image");
            Log.i("PP",uri);
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.default_pp_shape)
                    .circleCrop()
                    .into(holder.profile);
        }
        catch (JSONException e){
            e.getStackTrace();
            Log.i("Msg","Here");
        }
//        catch (Exception e){e.getStackTrace();
//            Log.i("Msg","Here");}
        }

    @Override
    public int getItemCount() {
        return Feed.length();
    }

    public void setFeed(JSONArray feed) {
        Feed = feed;
    }

    public class objViewHolder extends RecyclerView.ViewHolder{
        public TextView Content,User,date,likes,comments,options;
        public ImageView profile;
        public Button like,comment;
        Drawable[] drawables;
        int alt;
        MenuInflater menuInflater;
        public LinearLayout Card;
        public int id,post_id,isLiked;
        public objViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            Content=itemView.findViewById(R.id.bio);
            drawables=new Drawable[3];
            alt=0;
            drawables[0]=context.getDrawable(R.drawable.ic_heart);
            drawables[1]=context.getDrawable(R.drawable.ic_heartdislike);
            options=itemView.findViewById(R.id.options);
            if(!feedType.equals("ProfilePosts")){
                options.setVisibility(View.INVISIBLE);
            }
            User=itemView.findViewById(R.id.username);
            likes=itemView.findViewById(R.id.likes);
            comments=itemView.findViewById(R.id.comments);
            date=itemView.findViewById(R.id.email);
            profile=itemView.findViewById(R.id.profile_pic);
            Card=itemView.findViewById(R.id.Card);
            like=itemView.findViewById(R.id.Like);
            comment=itemView.findViewById(R.id.Comment);
//            menuInflater.inflate(R.menu.post_options);
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
                    getPost(context);
                }
            });
        }

        public void like(Context context,JSONArray feed,int pos){
            ++alt;
            like.setBackground(drawables[alt%2]);
            if(alt%2==0)
            {likes.setText(Integer.parseInt(likes.getText().toString())+1+"");}
            else
                {likes.setText(Integer.parseInt(likes.getText().toString())-1+"");}
            Log.i("msg","then this worked");
            Log.i("Liked","Liked");
            String url=context.getString(R.string.BASE_URL)+"/CMS-API/Feed/like";
            JSONObject postdata=new JSONObject();
            JSONArray ja=new JSONArray();
            try{
            postdata.put("id",post_id+"");
            ja.put(postdata);
            }
            catch (JSONException e){}
            RequestHandler req=new RequestHandler(false,url, Request.Method.POST,ja,context,sharedPreferences){
                @Override
                public void callbackError(VolleyError e) {
                    like.setBackground(drawables[2]);
                    super.callbackError(e);
                }

                @Override
                public void callback(JSONObject response) {
                    int s;
                    JSONObject js;
                    Log.i("Orig",response.toString());
                    Log.i("DataVar",response.toString());
//                    Log.i("S",s+"");
//                    likes.setText(s+"");
                    try{

                        Log.i("Action","Actually Liked");
                        s=response.getJSONObject("Data").getJSONObject("post").getInt("likes");
                        likes.setText(s+"");
                                                Log.i("Action","Actually Liked");
                        if(response.getString("Message").toString().equals("Liked")){
                            js=feed.getJSONObject(pos);
                            js.put("likes", s);
                            js.put("isLiked", 1);
                            Log.i("pos",""+pos);
                            feed.put(pos,js);
                            sharedPreferences.edit().putString(feedType,feed.toString()).apply();
                            like.setBackground(context.getDrawable(R.drawable.ic_heart));
                            Log.i("Action","Actually Liked");
                            Toast.makeText(context,"Liked",Toast.LENGTH_SHORT);
                        }
                        else if(response.getString("Message").toString().equals("Disliked")){
                            likes.setText(s+"");
                            Log.i("S",s+"");
                            js=feed.getJSONObject(pos);
                            js.put("likes", s);
                            js.put("isLiked", 0);
                            Log.i("pos",pos+"");
                            feed.put(pos,js);
                            sharedPreferences.edit().putString(feedType,feed.toString()).apply();
                            like.setBackground(context.getDrawable(R.drawable.ic_heartdislike));
                            Log.i("Action","Actually DisLiked");
//                            Toast.makeText(context,"Diliked",Toast.LENGTH_SHORT);
                        }
                        else{
                            Log.i("Action",response.getString("Message"));
                        }
                    }
                    catch (JSONException e){
                        Toast.makeText(context,"Liked",Toast.LENGTH_SHORT);
                        s = Integer.parseInt(likes.getText().toString())-1;
                        likes.setText(s+"");
                        e.getStackTrace();
                    }
                }
            };
        }
        public void getProfile(Context context){
            Intent i = new Intent(context, SubEvents.class);
//              Log.i("MyLogs",NotesList.size()+"");
            i.putExtra("id", id+"");
            context.startActivity(i);
        }
        public void getPost(Context context){
            Intent i = new Intent(context, SubEvents.class);
//              Log.i("MyLogs",NotesList.size()+"");
            i.putExtra("id", id+"");
            i.putExtra("post_id", post_id+"");
            context.startActivity(i);
        }


    }
}
