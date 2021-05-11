package com.vector.say_it;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

public class create_event extends AppCompatActivity {

    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        openFragment(new Create());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Navigations.class);
        this.startActivity(i);
        this.finish();
    }
    public void openFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.create_placeholder, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}