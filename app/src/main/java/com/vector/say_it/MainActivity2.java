package com.vector.say_it;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        setContentView(R.layout.activity_main2);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        super.onBackPressed();
    }
}