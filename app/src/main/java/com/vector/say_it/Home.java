package com.vector.say_it;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    public String url;
    static JSONArray data;
    ProgressBar progressBar;
    RecyclerView.LayoutManager layoutManager;
    feedVeiw FeedView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        data=new JSONArray();
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
        url=getString(R.string.BASE_URL)+"/CMS-API/Feed";
        RequestHandler req=new RequestHandler(true,url, Request.Method.GET,null,getActivity(),sharedPreferences){
            @Override
            public void callback(JSONArray response) {
                Log.i("Orig",response.toString());
                data=response;
                sharedPreferences.edit().putString("Feed",data.toString()).apply();
                Log.i("DataVar",data.toString());
                FeedView.setFeed(data);
                FeedView.notifyDataSetChanged();
            }
        };

//        cont();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void refresh(){
        RequestHandler req=new RequestHandler(true,url, Request.Method.GET,null,getActivity(),sharedPreferences){
            @Override
            public void callback(JSONArray response) {
//                Log.i("Orig",response.toString());
                data=response;
                sharedPreferences.edit().putString("Feed",data.toString()).apply();
//                Log.i("DataVar",data.toString());
                FeedView.setFeed(data);
                FeedView.notifyDataSetChanged();
            }
        };
    }
    public void restore(){
           try {
                data=new JSONArray(sharedPreferences.getString("Feed", ""));
                FeedView.setFeed(data);
                FeedView.notifyDataSetChanged();
                }
        catch (Exception e){ e.getStackTrace();}
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(sharedPreferences.getString("Feed", "").length()>=0){restore();}
        else{refresh();}
        View v;
        v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView= v.findViewById(R.id.FeedGUI);
//        progressBar=v.findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(getActivity());
        FeedView = new feedVeiw(data,getActivity(),sharedPreferences,"Feed");
        recyclerView.setLayoutManager(this.layoutManager);
        recyclerView.setAdapter(FeedView);
//        Log.i("DataVar",data.toString());
        return v;
    }
}