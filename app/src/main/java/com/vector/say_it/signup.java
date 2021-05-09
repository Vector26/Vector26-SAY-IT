package com.vector.say_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View v;
    SharedPreferences sharedPreferences;
    FragmentTransaction transaction;
    EditText username,f_name,l_name,email,password1,password2;
    TextView Alert;
    Button signup;
    String url;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signup.
     */
    // TODO: Rename and change types and number of parameters
    public static signup newInstance(String param1, String param2) {
        signup fragment = new signup();
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

    public void Login(View ve) {
        String user_text = username.getText().toString();
        String Fname=f_name.getText().toString();
        String Lname=l_name.getText().toString();
        String emailString=email.getText().toString();
        String pass_text = password1.getText().toString();
        String pass_text2 = password2.getText().toString();
        if(pass_text.equals(pass_text2)){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", user_text);
        params.put("first_name", Fname);
        params.put("last_name", Lname);
        params.put("email", emailString);
        params.put("password", pass_text);
        params.put("password2", pass_text2);
        RequestHandler req=new RequestHandler(false,url, Request.Method.POST,new JSONArray().put(new JSONObject(params)),getActivity(),sharedPreferences){

            @Override
            public void callback(JSONObject response) {
                String t;
                try{
                    t = response.getString("username");
                    if(t.equals(user_text)){
                        Alert.setText("Registerd, Please Log-in");
                        reset();
                    }
                    Log.i("Response", t);
                }
                catch (Exception e){
                    Alert.setText("Coud not sign-up");
                    Alert.setTextColor(v.getResources().getColor(R.color.Alert));
                    e.getStackTrace();
                    }
                }
            @Override
            public void callbackError(VolleyError e) {
                super.callbackError(e);
            }
        };
        }
        else{
            Alert.setText("Passwords do not match");
            Alert.setTextColor(v.getResources().getColor(R.color.Alert));
        }
    }

    public void reset(){
        username.setText("");
        f_name.setText("");
        l_name.setText("");
        email.setText("");
        password1.setText("");
        password2.setText("");
    }

    public void openFragment(Fragment fragment) {
        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.AuthFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void init()
    {
        sharedPreferences = getActivity().getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
        url = getString(R.string.BASE_URL)+"CMS-API/signUp";
        username = v.findViewById(R.id.login_username);
        f_name = v.findViewById(R.id.login_first_name);
        l_name = v.findViewById(R.id.login_lastname);
        email = v.findViewById(R.id.login_email);
        password1 = v.findViewById(R.id.login_password1);
        password2 = v.findViewById(R.id.login_password2);
        Alert=v.findViewById(R.id.titleSignup);
        signup=v.findViewById(R.id.login_button);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(v);
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_signup, container, false);
        init();
        return v;
    }
}