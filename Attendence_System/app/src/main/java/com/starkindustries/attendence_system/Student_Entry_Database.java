package com.starkindustries.attendence_system;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.starkindustries.attendence_system.Database.LogInDatabaseHandler;
import com.starkindustries.attendence_system.Model.Student;
import com.starkindustries.attendence_system.databinding.ActivityStudentEntryDatabaseBinding;

import java.util.ArrayList;
public class Student_Entry_Database extends AppCompatActivity {
    public ActivityStudentEntryDatabaseBinding binding;
    public ArrayList<String> year_list;
    public ArrayList<String> department_list;
    public ArrayList<String> division_list;
    public LogInDatabaseHandler db;
    public ArrayList<String> sid_arraylist;
    public ArrayAdapter<String> sid_arrayadapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_entry_database);
        binding= DataBindingUtil.setContentView(Student_Entry_Database.this,R.layout.activity_student_entry_database);
        year_list = new ArrayList<String>();
        department_list=new ArrayList<String>();
        division_list=new ArrayList<String>();
        year_list.add("FE");
        year_list.add("SE");
        year_list.add("TE");
        year_list.add("BE");
        department_list.add("Comps");
        department_list.add("IT");
        department_list.add("AI-DS");
        department_list.add("Electronics");
        department_list.add("Civil");
        department_list.add("Civil&Infra");
        department_list.add("Mechanical");
        department_list.add("Humanities");
        department_list.add("EX-TC");
        division_list.add("A");
        division_list.add("B");
        ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(Student_Entry_Database.this,android.R.layout.simple_list_item_1,year_list);
        binding.yearSpinner.setAdapter(year_adapter);
        ArrayAdapter<String> department_adapter = new ArrayAdapter<String>(Student_Entry_Database.this, android.R.layout.simple_list_item_1,department_list);
        binding.departmentSpinner.setAdapter(department_adapter);
        ArrayAdapter<String> division_adapter = new ArrayAdapter<String>(Student_Entry_Database.this, android.R.layout.simple_list_item_1,division_list);
        binding.divisionSpinner.setAdapter(division_adapter);
        db = new LogInDatabaseHandler(Student_Entry_Database.this);
        binding.addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.create_student(new Student(binding.sid.getText().toString().trim(),binding.name.getText().toString().trim(),binding.departmentSpinner.getSelectedItem().toString().trim(),
                        binding.divisionSpinner.getSelectedItem().toString().trim(),binding.yearSpinner.getSelectedItem().toString().trim(),binding.phoneNo.getText().toString().trim(),binding.email.getText().toString().trim()));
                Toast.makeText(Student_Entry_Database.this, "Student added sucessfully", Toast.LENGTH_SHORT).show();
                Log.d("count","The Count of Students is: "+db.student_count());
            }
        });
        binding.studentListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}