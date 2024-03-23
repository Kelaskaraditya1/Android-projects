package com.starkindustries.attendence_system;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.databinding.ActivityStudentDashboardBinding;
public class Student_Dashboard extends AppCompatActivity {
    public ActivityStudentDashboardBinding binding;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public FirebaseFirestore store;
    public SharedPreferences prefrence;
    public SharedPreferences.Editor edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);
        binding= DataBindingUtil.setContentView(Student_Dashboard.this,R.layout.activity_student_dashboard);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        store=FirebaseFirestore.getInstance();
        prefrence=getSharedPreferences(Keys.SHARED_PREFRANCE_NAME,MODE_PRIVATE);
        edit=prefrence.edit();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            binding.logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    Intent inext = new Intent(Student_Dashboard.this,Login_Activity.class);
                    edit.putBoolean(Keys.FLAG,false);
                    edit.apply();
                    startActivity(inext);
                }
            });
            return insets;
        });
    }
}