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

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText username,password;
    public String url;
    SharedPreferences sharedPreferences;
    View v;
    int flag;
    Button login;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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

    public void init(){
        flag=0;
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
        url = getString(R.string.BASE_URL)+"api-token-auth/";
        login=v.findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0){
                    flag=1;
                Login(v);}
            }
        });
    }
    public void Login(View ve) {
        username = v.findViewById(R.id.login_username);
        password = v.findViewById(R.id.login_password);
        String user_text = username.getText().toString();
        String pass_text = password.getText().toString();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", user_text);
        params.put("password", pass_text);
        RequestHandler req=new RequestHandler(false,url, Request.Method.POST,new JSONArray().put(new JSONObject(params)),getActivity(),sharedPreferences){

            @Override
            public void callback(JSONObject response) {
                String t;
                try{
                    t = response.getString("token");
                    Log.i("Response", t);
                    sharedPreferences.edit().putString("Auth-Token",t).apply();
                    Intent i = new Intent(getActivity(), Navigations.class);
                    getActivity().startActivity(i);
                    getActivity().finish();
                }
                catch (Exception e){
                    e.getStackTrace();
                }
            }
        };
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return v;
    }
}