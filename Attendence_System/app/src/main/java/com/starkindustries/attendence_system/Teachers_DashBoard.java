package com.starkindustries.attendence_system;
import androidx.annotation.Keep;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.databinding.ActivityTeachersDashBoardBinding;

public class Teachers_DashBoard extends AppCompatActivity {
    public ActivityTeachersDashBoardBinding binding;
    public SharedPreferences preferences;
    public SharedPreferences.Editor edit;
    public FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_dash_board);
        binding= DataBindingUtil.setContentView(Teachers_DashBoard.this,R.layout.activity_teachers_dash_board);
        auth= FirebaseAuth.getInstance();
        preferences=getSharedPreferences(Keys.SHARED_PREFRANCE_NAME,MODE_PRIVATE);
        edit=preferences.edit();
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent inext = new Intent(Teachers_DashBoard.this,Login_Activity.class);
                edit.putBoolean(Keys.FLAG,false);
                startActivity(inext);
            }
        });
        binding.database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inext = new Intent(Teachers_DashBoard.this,Student_Entry_Database.class);
                startActivity(inext);
            }
        });
    }
}