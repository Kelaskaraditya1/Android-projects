package com.starkindustries.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.starkindustries.triviaapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainBinding binding= DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        Animation animate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo_animation);
        binding.logo.startAnimation(animate);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pair pairs[] = new Pair[2];
                Intent inext = new Intent(getApplicationContext(),Main_Screen.class);
                pairs[0]=new Pair<View,String>(binding.logo,"app_logo");
                pairs[1]=new Pair<View,String>(binding.name,"app_name");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(inext,options.toBundle());
                finish();
            }
        },2000);
    }
}