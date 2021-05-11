package com.vector.say_it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity4 extends AppCompatActivity {

    public FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        if(getIntent().getIntExtra("action",0)==0){
        int state=getIntent().getIntExtra("state",0);
        Bundle bundle = new Bundle();
        bundle.putInt("state",state);
// set Fragmentclass Arguments
        FollowerFragment fragobj = new FollowerFragment();
        fragobj.setArguments(bundle);
        openFragment(fragobj);
        }
        else{

        }
    }
    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_field, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}