package com.starkindustries.attendence_system;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.starkindustries.attendence_system.Database.LogInDatabaseHandler;
import com.starkindustries.attendence_system.databinding.ActivityMainBinding;
import com.starkindustries.attendence_system.Keys.Keys;

import java.security.Key;

public class MainActivity extends AppCompatActivity {
public ActivityMainBinding binding;
public Animation scale_animation;
public Animation alpha_animation;
public SharedPreferences preferences;
public SharedPreferences.Editor editor;
public FirebaseAuth auth;
public FirebaseFirestore store;
public DocumentReference refrence;
public String user_id;
public LogInDatabaseHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();

        scale_animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.scale_animation);
        alpha_animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.alpha_animation);
        binding.logo.startAnimation(scale_animation);
        binding.name.startAnimation(alpha_animation);
        preferences = getSharedPreferences(Keys.SHARED_PREFRANCE_NAME,MODE_PRIVATE);
        editor = preferences.edit();
        handler=new LogInDatabaseHandler(MainActivity.this);
        handler.getRegisteredCount();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean flag = preferences.getBoolean(Keys.FLAG, false);
                if (flag) {
//                    Intent inext = new Intent(MainActivity.this, Student_Dashboard.class);
//                    startActivity(inext);
//                    finish();
//                    try{
//                        user_id=auth.getCurrentUser().getUid();
//                        refrence=store.collection(Keys.COLLECTION_NAME).document(user_id);
//                        if(auth.getCurrentUser()!=null)
//                        {
//                            refrence.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    if(value.exists())
//                                    {
//                                        String user_type=(String)value.get(Keys.USER_TYPE);
//                                        if(user_type!=null)
//                                        {
//                                            if(user_type.equals(Keys.STUDENT))
//                                            {
//                                                Intent inext = new Intent(MainActivity.this,Student_Dashboard.class);
//                                                startActivity(inext);
//                                            }
//                                            else if(user_type.equals(Keys.TEACHER))
//                                            {
//                                                Intent inext = new Intent(MainActivity.this,Teachers_DashBoard.class);
//                                                startActivity(inext);
//                                            }
//                                        }
//
//                                    }
//                                }
//                            });
//                        }
//                    }catch(Exception e)
//                    {
//                        e.printStackTrace();
//                    }
                    try
                    {
                        String user_type=(String)preferences.getString(Keys.USER_TYPE,Keys.STUDENT);
                        if(user_type.equals(Keys.TEACHER))
                        {
                            Intent inext = new Intent(MainActivity.this,Teachers_DashBoard.class);
                            finish();
                            startActivity(inext);
                        }
                        else {
                            Intent inext = new Intent(MainActivity.this,Student_Dashboard.class);
                            finish();
                            startActivity(inext);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                } else {
                    Intent inext = new Intent(MainActivity.this, Login_Activity.class);
                    Pair pairs[] = new Pair[2];
                    pairs[0] = new Pair<View, String>(binding.logo, "app_logo");
                    pairs[1] = new Pair<View, String>(binding.name, "app_name");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(inext, options.toBundle());
                    finish();
                }
            }
        },2000);
    }
}