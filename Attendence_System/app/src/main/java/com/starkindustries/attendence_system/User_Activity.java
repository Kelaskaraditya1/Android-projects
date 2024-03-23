package com.starkindustries.attendence_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.starkindustries.attendence_system.databinding.ActivityUserBinding;

public class User_Activity extends AppCompatActivity {
    ActivityUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        binding= DataBindingUtil.setContentView(User_Activity.this,R.layout.activity_user);
//        binding.student.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent inext = new Intent(User_Activity.this, Student_Register.class);
//                startActivity(inext);
//            }
//        });
        binding.studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inext = new Intent(User_Activity.this,Student_Register.class);
                startActivity(inext);
            }
        });
        binding.teacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inext = new Intent(User_Activity.this, Teacher_Register.class);
                startActivity(inext);
            }
        });
    }
}