package com.vector.say_it;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

public class MainActivity0 extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    public String url;
    RecyclerView.LayoutManager layoutManager;
    feedVeiw FeedView;
    Search search;
    Home home;
    Stack<Integer> arrayList;
    int[] action_bars;
    ProfileFragment profileFragment;
    FragmentTransaction transaction;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        sharedPreferences = getSharedPreferences("com.vector.say_it", MODE_PRIVATE);
        sharedPreferences.edit().putString("Feed","").apply();
        sharedPreferences.edit().putString("Profile","").apply();
        sharedPreferences.edit().putString("ProfilePosts","").apply();
        sharedPreferences.edit().putString("Search_Results","").apply();
        arrayList=new Stack<>();
        action_bars= new int[]{R.layout.custom_action_bar,R.layout.search_action_bar,R.layout.profile_action_bar};
        if(sharedPreferences.getString("Auth-Token", "").length()<=1){
            Intent i = new Intent(MainActivity0.this, MainActivity.class);
//            Log.i("Auth-Token","->"+sharedPreferences.getString("Auth-Token", ""));
            this.startActivity(i);
            finish();
        }
        else{
            setContentView(R.layout.activity_main0);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
            Log.i("Auth-Token","->"+sharedPreferences.getString("Auth-Token", ""));
            cont_create();
        }

    }


    public void cont_create(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        home = new Home();
        search = new Search();
        profileFragment=new ProfileFragment();
        bottomNavigationView=findViewById(R.id.navigator);
        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
                        openFragment(home);
                        arrayList.push(action_bars[0]);
                        return true;
                    case R.id.page_2:
                        getSupportActionBar().setCustomView(R.layout.search_action_bar);
                        openFragment(search);
                        arrayList.push(action_bars[1]);
                        Log.i("Fragment","Search Cliked");
                        return true;
                    case R.id.page_3:
                        getSupportActionBar().setCustomView(R.layout.profile_action_bar);
                        openFragment(profileFragment);
                        arrayList.push(action_bars[2]);
                        Log.i("Fragment","profile cliked");
                        return true;
                    case R.id.page_4:
                        Log.i("Fragment","create cliked");
                        return true;
                }
                return false;
            }
        };
//        BottomNavigationView.OnNavigationItemReselectedListener navigationItemReselectedListener=new BottomNavigationView.OnNavigationItemReselectedListener() {
//            @Override
//            public void onNavigationItemReselected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.page_1:
//                        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
//                        home.refresh();
//                        break;
//                    case R.id.page_2:
//                        getSupportActionBar().setCustomView(R.layout.search_action_bar);
//                        search.refresh();
//                        Log.i("Fragment","Search Cliked");
//                        break;
//                    case R.id.page_3:
//                        getSupportActionBar().setCustomView(R.layout.profile_action_bar);
//                        profileFragment.refresh();
//                        Log.i("Fragment","profile cliked");
//                        break;
//                    case R.id.page_4:
//                        Log.i("Fragment","create cliked");
//                        break;
//                }
//            }
//        };
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//        bottomNavigationView.setOnNavigationItemReselectedListener(navigationItemReselectedListener);
        openFragment(home);
        arrayList.push(action_bars[0]);
    }
    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentGround, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        arrayList.pop();
        try{getSupportActionBar().setCustomView(arrayList.lastElement().intValue());}
        catch (NoSuchElementException e){
            e.getStackTrace();
            finish();
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}