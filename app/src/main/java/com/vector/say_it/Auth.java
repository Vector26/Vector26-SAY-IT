package com.vector.say_it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Auth extends AppCompatActivity {

    EditText username,password;
    public String url;
    FragmentTransaction transaction;
    SharedPreferences sharedPreferences;
    Button signup;
    int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signup=findViewById(R.id.signup);
        counter=0;
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter%2==0){
                    signup.setText("LOGIN");
                    openFragment(new signup());
                    counter++;
                }
                else{
                    signup.setText("SIGNIN");
                    openFragment(new Login());
                    counter++;
                }
            }
        });
        sharedPreferences = getSharedPreferences("com.vector.say_it", MODE_PRIVATE);
        url = getString(R.string.BASE_URL)+"api-token-auth/";
        openFragment(new Login());
    }


    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.AuthFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
