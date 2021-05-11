package com.vector.say_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Create#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Create extends Fragment {


    View v;
    String imgurl;
    ImageView profile_pic;
    JSONObject data2;
    EditText blog;
    Button send_it;
    SharedPreferences sharedPreferences;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Create() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Create.
     */
    // TODO: Rename and change types and number of parameters
    public static Create newInstance(String param1, String param2) {
        Create fragment = new Create();
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
    }

    public void downloadPP(){
        String url=getString(R.string.BASE_URL)+"CMS-API";
        RequestHandler req = new RequestHandler(false, url, Request.Method.GET, null, getActivity(), sharedPreferences){
            @Override
            public void callback(JSONObject response) {
                sharedPreferences.edit().putString("Profile", response.toString()).apply();
                restore();
            }
        };
    }

    public void PostBlog(){
        String post_url=getString(R.string.BASE_URL)+"CMS-API/Feed";
        if(blog.getText().length()>0){
        HashMap<String,String> params=new HashMap<>();
        params.put("content",blog.getText().toString());
        RequestHandler req=new RequestHandler(false,post_url, Request.Method.POST,new JSONArray().put(new JSONObject(params)),getActivity(),sharedPreferences){
            @Override
            public void callback(JSONObject response) {
                Log.i("response",response.toString());
                try{JSONArray temp=new JSONArray(sharedPreferences.getString("Feed", ""));
                temp.put(response);
                sharedPreferences.edit().putString("Feed",temp.toString()).apply();}
                catch (JSONException e){e.getStackTrace();}
                finally {
                    Intent i = new Intent(getActivity(), Navigations.class);
                    getActivity().startActivity(i);
                    getActivity().finish();
                    getActivity().finish();
                }
                }
            };
        }
    }

    public void init(){
        profile_pic=v.findViewById(R.id.PP);
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
        blog=v.findViewById(R.id.Blog);
        send_it=v.findViewById(R.id.post_it);
        send_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostBlog();
            }
        });
        if(sharedPreferences.getString("ProfilePosts", "").length()>0){
            Log.i("Msg","Exists and n0t id");
            restore();
        }
        else{
            downloadPP();
        }
    }

    public void restore(){
        try{
            data2=new JSONObject(sharedPreferences.getString("Profile", ""));
           String uri = getActivity().getString(R.string.BASE_URL)+data2.getJSONObject("Profile").getString("image");
           Log.i("uri",uri);
            Glide.with(getActivity())
                    .load(uri)
                    .placeholder(R.drawable.default_pp_shape)
                    .circleCrop()
                    .into(profile_pic);
        }
        catch (JSONException e){e.getStackTrace();}
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_create, container, false);
        init();
        return v;
    }
}