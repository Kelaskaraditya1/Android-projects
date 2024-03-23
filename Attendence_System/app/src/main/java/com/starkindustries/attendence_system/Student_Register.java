package com.starkindustries.attendence_system;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.starkindustries.attendence_system.Database.LogInDatabaseHandler;
import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.Model.User;
import com.starkindustries.attendence_system.databinding.ActivityStudentRegisterBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class Student_Register extends AppCompatActivity {
    public ActivityStudentRegisterBinding binding;
    public ArrayList<String> year_list;
    public ArrayList<String> department_list;
    public ArrayList<String> division_list;
    public boolean pass_ed;
    public FirebaseAuth auth;
    public FirebaseFirestore store;
    public SharedPreferences prefrences;
    public LogInDatabaseHandler handler;
    public SharedPreferences.Editor edit;
    public String user_id;
    public StorageReference reference,child_refrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        binding= DataBindingUtil.setContentView(Student_Register.this,R.layout.activity_student_register);
        auth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        handler=new LogInDatabaseHandler(Student_Register.this);
        prefrences=getSharedPreferences(Keys.SHARED_PREFRANCE_NAME,MODE_PRIVATE);
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
        ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(Student_Register.this,android.R.layout.simple_list_item_1,year_list);
        binding.yearSpinner.setAdapter(year_adapter);
        ArrayAdapter<String> department_adapter = new ArrayAdapter<String>(Student_Register.this, android.R.layout.simple_list_item_1,department_list);
        binding.departmentSpinner.setAdapter(department_adapter);
        ArrayAdapter<String> division_adapter = new ArrayAdapter<String>(Student_Register.this, android.R.layout.simple_list_item_1,division_list);
        binding.divisionSpinner.setAdapter(division_adapter);
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gellery = new Intent(Intent.ACTION_PICK);
                gellery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gellery,Keys.GALLERY_PROFILE_IMAGE);
            }
        });
        binding.password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int right=2;
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=(binding.password.getRight()-binding.password.getCompoundDrawables()[right].getBounds().width()))
                    {
                        int selection=binding.password.getSelectionEnd();
                        if(pass_ed)
                        {
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_off,0);
                            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pass_ed=false;
                        }
                        else
                        {
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_on,0);
                            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pass_ed=true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        binding.confirmPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int right=2;
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=(binding.confirmPass.getRight()-binding.password.getCompoundDrawables()[right].getBounds().width()))
                    {
                        int selection=binding.confirmPass.getSelectionEnd();
                        if(pass_ed)
                        {
                            binding.confirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_off,0);
                            binding.confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pass_ed=false;
                        }
                        else
                        {
                            binding.confirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_on,0);
                            binding.confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pass_ed=true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(),0);
                if(TextUtils.isEmpty(binding.sid.getText().toString().trim())) {
                    binding.sid.setError("Enter Proper Student_ID");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.name.getText().toString().trim())) {
                    binding.name.setError("Enter Proper Name");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.phoneNo.getText().toString().trim())) {
                    binding.phoneNo.setError("Enter Proper Phone number");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.username.getText().toString().trim())) {
                    binding.username.setError("Enter Proper username");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.email.getText().toString().trim()))
                {
                    binding.email.setError("Enter proper Email");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.password.getText().toString().trim())) {
                    binding.password.setError("Enter Proper Password");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.confirmPass.getText().toString().trim())) {
                    binding.confirmPass.setError("Enter Proper Password");
                    return ;
                }
                else if(binding.password.getText().toString().length()<8) {
                    binding.password.setError("Password lenget should be greater than 6 charecters");
                    return ;
                }
                else if(!binding.password.getText().toString().trim().equals(binding.confirmPass.getText().toString().trim()))
                {
                    binding.password.setError("Password and confirm password doesn't match");
                    binding.confirmPass.setError("Password and confirm password doesn't match");
                    return ;
                }
                auth.createUserWithEmailAndPassword(binding.email.getText().toString().trim(),binding.password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            user_id=auth.getCurrentUser().getUid();
                            DocumentReference refrences = store.collection(Keys.COLLECTION_NAME).document(user_id);
                            HashMap<String,Object> map = new HashMap<String,Object>();
                            map.put(Keys.SID,binding.sid.getText().toString().trim());
                            map.put(Keys.NAME,binding.name.getText().toString().trim());
                            map.put(Keys.YEAR,binding.yearSpinner.getSelectedItem().toString().trim());
                            map.put(Keys.DEPARTMENT,binding.departmentSpinner.getSelectedItem().toString().trim());
                            map.put(Keys.DIVISION,binding.divisionSpinner.getSelectedItem().toString().trim());
                            map.put(Keys.PHONE_NO,binding.phoneNo.getText().toString().trim());
                            map.put(Keys.EMAIL,binding.email.getText().toString().trim());
                            map.put(Keys.USER_TYPE,Keys.STUDENT);
                            map.put(Keys.USERNAME,binding.username.getText().toString().trim());
                            map.put(Keys.PASSWORD,binding.password.getText().toString().trim());
                            User user = new User();
                            user.setUsername(binding.sid.getText().toString().trim());
                            user.setPassword(binding.password.getText().toString().trim());
                            user.setName(binding.name.getText().toString().trim());
                            user.setUser_type(Keys.STUDENT);
                            handler.insertUser(user);

                            refrences.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Student_Register.this, "Registered Sucessfully", Toast.LENGTH_SHORT).show();
                                    Intent inext = new Intent(Student_Register.this, Photos_Upload_Activity.class);
                                    startActivity(inext);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Student_Register.this, "Something went wrong,check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(Student_Register.this, "Check your internet connection\nEither email is already taken", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==Keys.GALLERY_PROFILE_IMAGE)
            {
                binding.profileImage.setImageURI(data.getData());
                Uri image_uri = data.getData();
                reference=FirebaseStorage.getInstance().getReference();
                child_refrence=reference.child(Keys.STUDENT_PROFILE_IMAGE);
                child_refrence.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Student_Register.this, "Profile Image uploaded sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("fail_transfer",e.getMessage().toString());
                    }
                });
            }
        }
    }
}