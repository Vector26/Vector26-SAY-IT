package com.vector.say_it;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowerFragment extends Fragment {


    View v;
    int state,id;
    RecyclerView recyclerView;
    String[] states;
    RecyclerView.LayoutManager layoutManager;
    JSONArray followList;
    followersVeiw FollowersVeiw;
    LinearLayout waiter;
    SharedPreferences sharedPreferences;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FollowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowerFragment newInstance(String param1, String param2) {
        FollowerFragment fragment = new FollowerFragment();
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

    public void refresh(){
        waiter.setVisibility(View.VISIBLE);
        String url=getString(R.string.BASE_URL)+"CMS-API/follow";
        if(id>0)
            url=url+"?id="+id;
        Log.i("FURL->>",url);
        RequestHandler req=new RequestHandler(false,url, Request.Method.GET,null,getActivity(),sharedPreferences){
            @Override
            public void callback(JSONObject response) {
                try{
                    Log.i("FURL->>",response.toString());
                    FollowersVeiw.setFeed(response.getJSONArray(states[state]));
                    FollowersVeiw.notifyDataSetChanged();
                    waiter.setVisibility(View.GONE);
                }
                catch (JSONException e){e.getStackTrace();}
            }
        };
    }
    public void init(){
        states=new String[2];
        states[0]="followers";
        states[1]="followings";
            id=0;
            waiter=v.findViewById(R.id.waiter);
            recyclerView= v.findViewById(R.id.followerList);
            layoutManager = new LinearLayoutManager(getActivity());
            followList=new JSONArray();
            FollowersVeiw=new followersVeiw(followList,getActivity(),state);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(FollowersVeiw);
            sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
            if(getActivity().getIntent().getStringExtra("id").length()>0){
                id=Integer.parseInt(getActivity().getIntent().getStringExtra("id"));
                }
            refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_follower, container, false);
        // Inflate the layout for this fragment
        Bundle b=this.getArguments();
        state=b.getInt("state");
        init();
        return v;
    }
}