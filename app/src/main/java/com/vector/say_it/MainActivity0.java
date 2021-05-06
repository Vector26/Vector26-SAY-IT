package com.vector.say_it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity0 extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    public String url;
    RecyclerView.LayoutManager layoutManager;
    feedVeiw FeedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        sharedPreferences = getSharedPreferences("com.vector.say_it", MODE_PRIVATE);
        if(sharedPreferences.getString("Auth-Token", "").length()<=1){
            Intent i = new Intent(MainActivity0.this, MainActivity.class);
//            Log.i("Auth-Token","->"+sharedPreferences.getString("Auth-Token", ""));
            this.startActivity(i);
            finish();
        }
        else{
            setContentView(R.layout.activity_main0);
            Log.i("Auth-Token","->"+sharedPreferences.getString("Auth-Token", ""));
            layoutManager = new LinearLayoutManager(this);
            FeedView = new feedVeiw(new JSONArray(),this);
            recyclerView=(RecyclerView) findViewById(R.id.FeedGUI);
            recyclerView.setLayoutManager(this.layoutManager);
            recyclerView.setAdapter(FeedView);
            cont_create();
        }

    }

    public void cont_create(){
        url=getString(R.string.BASE_URL)+"/CMS-API/Feed";
        RequestHandler req=new RequestHandler(true,url,Request.Method.GET,null,this,sharedPreferences){

            @Override
            public void callback(JSONArray response) {
                    Log.i("Orig",response.toString());
                    FeedView.setFeed(response);
                    FeedView.notifyDataSetChanged();
            }
        };
    }
    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        finish();
        super.onBackPressed();
    }
}