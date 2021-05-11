package com.vector.say_it;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    SwipeRefreshLayout swipeRefreshLayout;
    int id,follow_status;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    LinearLayout linearLayout,getFollowers,getFollowed;;
    ProgressBar progressBar;
    public String url,uri,follow_req;
    public FragmentTransaction transaction;
    static JSONArray data;
    JSONObject data2;
    RecyclerView.LayoutManager layoutManager;
    feedVeiw FeedView;
    String [] savePoints;
    TextView profile_options,userHeader,username,email,first_name,bio,posts_count,Followed_count,followers_count;
    ImageView profile_pic;
    Button follow;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
//        url=getString(R.string.BASE_URL)+"/CMS-API";
    }
    public void refresh() {
        if(id>-1){
            savePoints[0]="GuestProfile"+id;
            savePoints[1]="GuestProfilePosts"+id;
            url=getString(R.string.BASE_URL)+"/CMS-API";
            url=url+"?id="+id;
        }
        else{
            savePoints[0]="Profile";
            savePoints[1]="ProfilePosts";
            url=getString(R.string.BASE_URL)+"/CMS-API";
        }
        Log.i("Msg","url->>"+url);
        linearLayout.setVisibility(View.VISIBLE);
        RequestHandler req = new RequestHandler(false, url, Request.Method.GET, null, getActivity(), sharedPreferences) {
            @Override
            public void callback(JSONObject response) {
                try {
                    linearLayout.setVisibility(View.GONE);
                    data2=response;
                    Log.i("Msg","data2->>>"+data2.toString());
                    sharedPreferences.edit().putString(savePoints[0], data2.toString()).apply();
                    data = data2.getJSONArray("Posts");
                    Log.i("Msg","data->>>"+data.toString());
                    sharedPreferences.edit().putString(savePoints[1], data.toString()).apply();
                }
                catch (Exception e){e.getStackTrace();
                data=new JSONArray();
                Log.i("Msg","data->>>"+data.toString());
                }
                finally {
                    restore();
                }
            }
        };
    }
    public void restore(){
        try {
            if(id>-1){
                Log.i("Msg","Exists and id");
                data2=new JSONObject(sharedPreferences.getString("GuestProfile"+id, ""));
                data=new JSONArray(sharedPreferences.getString("GuestProfilePosts"+id, ""));
            }
            else{
                Log.i("Msg","Exists and n0t id");
                data2=new JSONObject(sharedPreferences.getString("Profile", ""));
                data=new JSONArray(sharedPreferences.getString("ProfilePosts", ""));
            }
            linearLayout.setVisibility(View.GONE);
        }
        catch (Exception e){ e.getStackTrace();data=new JSONArray();}
        finally {
            Log.i("Msgrestorre","data->"+data.toString());
            Log.i("Msg","data2->"+data2.toString());
            setDataVALUES();
            if(data.length()>0){
                v.findViewById(R.id.NoPostL).setVisibility(View.GONE);
            }
            else{
                v.findViewById(R.id.NoPostL).setVisibility(View.VISIBLE);
            }

            FeedView.setFeed(data);
            FeedView.notifyDataSetChanged();
//            onDestroy();
        }
    }

    public void setDataVALUES(){
        try{
            Log.i("Orig",data2.toString());
            isItselfforFollow();
            username.setText(data2.getJSONObject("Profile").getJSONObject("user").getString("username"));
            userHeader.setText("@"+username.getText().toString());
            email.setText(data2.getJSONObject("Profile").getJSONObject("user").getString("email"));
            Log.i("Orignal",data2.toString());
            first_name.setText(data2.getJSONObject("Profile").getJSONObject("user").getString("first_name")+" "+data2.getJSONObject("Profile").getJSONObject("user").getString("last_name"));
            followers_count.setText(data2.getJSONObject("Profile").getString("FRC"));
            Followed_count.setText(data2.getJSONObject("Profile").getString("FDC"));
            bio.setText(data2.getJSONObject("Profile").getString("Bio"));
//            Log.i("url","Hello");
            posts_count.setText(data2.getJSONObject("Profile").getString("PC"));
            uri = getActivity().getString(R.string.BASE_URL)+data2.getJSONObject("Profile").getString("image");
//            Log.i("url",uri+"Hello");
            Glide.with(getActivity())
                    .load(uri)
                    .placeholder(R.drawable.default_pp_shape)
                    .circleCrop()
                    .into(profile_pic);
        }
        catch (JSONException e){e.getStackTrace();
            Log.i("url","HelloJSON");}
        catch (Exception e){e.getStackTrace();
//            Log.i("url","Hello");
        }
    }

    public void isItselfforFollow(){
        try{
            JSONArray temp=data2.getJSONArray("Posts");
            if(data2.getInt("isItself")==1){
            Log.i("msg=","This worked");
                Log.i("isItself",data2.getInt("isItself")+"");
                follow.setVisibility(View.GONE);
                follow_status=0;
            }
            follow.setText("Unfollow");
            follow.setTextColor(getResources().getColor(R.color.black));
            follow.setBackground(getResources().getDrawable(R.drawable.text_fields_shape2));
        }
        catch (JSONException e) {
            try {
                if(data2.getInt("isItself")==1){
                    Log.i("isItself",data2.getInt("isItself")+"");
                    follow.setVisibility(View.GONE);
                }
                else{
                follow.setText("Follow");
                follow.setTextColor(getResources().getColor(R.color.white));
                follow.setBackground(getResources().getDrawable(R.drawable.button_shape));
                follow_status=1;
                }
            }
            catch (JSONException t){}
        }
    }

    public void delete(int post){
        Log.i("post->",post+"");
        String uri=getString(R.string.BASE_URL)+"CMS-API/Feed?id="+post;
        RequestHandler req = new RequestHandler(false, uri, Request.Method.DELETE, null, getActivity(), sharedPreferences) {
            @Override
            public void callbackError(VolleyError e) {
                Log.i("post->Error",post+"");
                Log.i("post->Error",e.getMessage()+"");
                super.callbackError(e);
            }

            @Override
            public void callback(JSONObject response) {
                Log.i("post->", post + "");
                refresh();
            }
        };

    }

    public void init(){
        data=new JSONArray();
        id=-1;
        getFollowers=v.findViewById(R.id.getFollowers);
        getFollowed=v.findViewById(R.id.getFollowed);
        progressBar=v.findViewById(R.id.progressBar);
        linearLayout=v.findViewById(R.id.waiter);
        progressBar.isIndeterminate();
        follow_req=getString(R.string.BASE_URL)+"CMS-API/follow";
        follow=v.findViewById(R.id.followButton);
        savePoints=new String[2];
        data2=new JSONObject();
        recyclerView= v.findViewById(R.id.userPosts);
        layoutManager = new LinearLayoutManager(getActivity());
        FeedView=new feedVeiw(data,getActivity(),sharedPreferences,"ProfilePosts",this);
        recyclerView.setLayoutManager(this.layoutManager);
        recyclerView.setAdapter(FeedView);
        username=v.findViewById(R.id.username);
        email=v.findViewById(R.id.email);
        posts_count=v.findViewById(R.id.PostsVal);
        userHeader=getActivity().findViewById(R.id.GuestUserId);
        profile_options=getActivity().findViewById(R.id.profile_options);
        profile_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), v);
                //inflating menu from xml resource
                popup.inflate(R.menu.profile_options);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.logout:
                                //handle menu1 click
                                sharedPreferences.edit().putString("Auth-Token","").apply();
                                Intent i = new Intent(getActivity(), Auth.class);
                                getActivity().startActivity(i);
                                getActivity().finish();
                                break;
                            case R.id.edit_profile:

                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
        Followed_count=v.findViewById(R.id.FollowedVal);
        followers_count=v.findViewById(R.id.followersVal);
        first_name=v.findViewById(R.id.first_name);
        bio=v.findViewById(R.id.Poriflebio);
        profile_pic=v.findViewById(R.id.profile_pic);
        swipeRefreshLayout = v.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        // Your code goes here
                        // In this code, we are just
                        // changing the text in the textbox
                        refresh();
                        // This line is important as it explicitly
                        // refreshes only once
                        // If "true" it implicitly refreshes forever
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        if(getActivity().getIntent().getStringExtra("id")!=null){
            id=Integer.parseInt(getActivity().getIntent().getStringExtra("id"));
            Log.i("id",id+"");
            if(sharedPreferences.getString("GuestProfilePosts"+id, "").length()>=1){
                Log.i("Msg","Exists and id");
                restore();
            }
            else{
                Log.i("Msg","n0t Exists and id");
                refresh();
            }
        }
        else{
        if(sharedPreferences.getString("ProfilePosts", "").length()>0){
            Log.i("Msg","Exists and n0t id");
            restore();
        }
        else{
            Log.i("Msg","n0t Exists and n0t id");
            refresh();}
        }
        getFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity4.class);
//              Log.i("MyLogs",NotesList.size()+"");
                i.putExtra("id", id+"");
                i.putExtra("state", 0);
                getActivity().startActivity(i);
            }
        });
        getFollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity4.class);
//              Log.i("MyLogs",NotesList.size()+"");
                i.putExtra("id", id+"");
                i.putExtra("state", 1);
                getActivity().startActivity(i);
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onFollow();
                YoYo.with(Techniques.FadeIn)
                        .duration(500)
                        .playOn(v);
            }
        });
    }

    public void onFollow(){
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("id",id+"");
            linearLayout.setVisibility(View.GONE);
            RequestHandler req=new RequestHandler(false,follow_req, Request.Method.POST,new JSONArray().put(new JSONObject(params)),getActivity(),sharedPreferences){

                @Override
                public void callback(JSONObject response) {
                    linearLayout.setVisibility(View.VISIBLE);
                    String t;
                    try{
                        t = response.getString("Message");
                        if(t.equals("Followed")){

//                            refresh();
                        }
                        else if(t.equals("Unfollowed")){

//                            refresh();
                        }
                        Log.i("Response", t);
                    }
                    catch (JSONException e){
                        Toast.makeText(getActivity(),"Unable to Follow",Toast.LENGTH_SHORT);
//                        refresh();
                        Log.i("MSG","unfollowed");
                        e.getStackTrace();
                    }
                    finally {
                        sharedPreferences.edit().putString(savePoints[0], "").apply();
                        sharedPreferences.edit().putString(savePoints[1], "").apply();
                        refresh();
                    }
                }
                @Override
                public void callbackError(VolleyError e) {
                    super.callbackError(e);
                    Toast.makeText(getActivity(),"Unable to Follow",Toast.LENGTH_SHORT);
                }
            };
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return v;
    }
}