package com.vector.say_it;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity {

    public FragmentTransaction transaction;
    SharedPreferences sharedPreferences;
    Button back;
    Button follow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        setContentView(R.layout.activity_main2);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.guest_profile_action_bar);
        sharedPreferences = getSharedPreferences("com.vector.say_it", Context.MODE_PRIVATE);
//        follow=findViewById(R.id.followButton);
//        follow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProfileFragment pf=new ProfileFragment();
//                openFragment(pf);
//            }
//        });
        if(getIntent().getStringExtra("post_id")!=null){
            openFragment(new single_post().newInstance("",""));
        }
        else{
        openFragment(new ProfileFragment().newInstance("",""));
        }
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profileView, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        sharedPreferences.edit().putString("GuestProfile"+getIntent().getStringExtra("id"), "").apply();
        sharedPreferences.edit().putString("GuestProfilePosts"+getIntent().getStringExtra("id"), "").apply();
        finish();
    }
}