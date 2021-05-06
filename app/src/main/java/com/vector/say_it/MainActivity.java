package com.vector.say_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    EditText username,password;
    public String url;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("com.vector.say_it", MODE_PRIVATE);
        url = getString(R.string.BASE_URL)+"/api-token-auth/";
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    public void Login(View v) {
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        String user_text = username.getText().toString();
        String pass_text = password.getText().toString();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("username", user_text);
            params.put("password", pass_text);
            RequestHandler req=new RequestHandler(false,url,Request.Method.POST,new JSONArray().put(new JSONObject(params)),this,sharedPreferences){

                @Override
                public void callback(JSONObject response) {
                    String t;
                    try{
                        t = response.getString("token");
                        Log.i("Response", t);
                        sharedPreferences.edit().putString("Auth-Token",t).apply();
                        Intent i = new Intent(MainActivity.this, MainActivity0.class);
                        MainActivity.this.startActivity(i);
                        finish();
                    }
                    catch (Exception e){
                        e.getStackTrace();
                    }
                }
            };
    }
}
