package com.vector.say_it;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Search#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Search extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    View v;
    Button SB;
    EditText query;
    SharedPreferences sharedPreferences;
    static JSONArray data;
    SearchVeiw searchVeiw;
    String url;
    RecyclerView.LayoutManager layoutManager;
    public Search() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Search.
     */
    // TODO: Rename and change types and number of parameters
    public static Search newInstance(String param1, String param2) {
        Search fragment = new Search();
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
        RequestHandler req=new RequestHandler(false,url, Request.Method.GET,null,getActivity(),sharedPreferences){
            @Override
            public void callback(JSONObject response) {
                Log.i("Orig",response.toString());
                try{
                    data=response.getJSONArray("data");
                    Log.i("DataVar",data.toString());
                    sharedPreferences.edit().putString("Search_Results",data.toString()).apply();
                    searchVeiw.setFeed(data);
                    searchVeiw.notifyDataSetChanged();
                }
                catch (Exception e){e.getStackTrace();}
//                sharedPreferences.edit().putString("Feed",data.toString()).apply();
            }
        };
    }
    public void init()
    {
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
        query = this.getActivity().findViewById(R.id.searchTE);
        url=getString(R.string.BASE_URL)+"/CMS-API/search?username=";
    }
    public void restore(){
        try {
            data=new JSONArray(sharedPreferences.getString("Search_Results", ""));
            searchVeiw.setFeed(data);
            searchVeiw.notifyDataSetChanged();
        }
        catch (Exception e){ e.getStackTrace();}
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search, container, false);
        init();
        data=new JSONArray();
        if(sharedPreferences.getString("Search_Results", "").length()>=0){restore();}
        else{refresh();}
        recyclerView= v.findViewById(R.id.SearchGUI);
        layoutManager =new LinearLayoutManager(getActivity());
        searchVeiw = new SearchVeiw(data,getActivity());
        recyclerView.setLayoutManager(this.layoutManager);
        recyclerView.setAdapter(searchVeiw);
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("Before",query.getText().toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                url=getString(R.string.BASE_URL)+"/CMS-API/search?username="+query.getText();
                Log.i("DataVar","Button pressed");
                refresh();
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("After",query.getText().toString());
            }
        });
        return v;
    }
}