package com.starkindustries.attendence_system;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
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
import com.starkindustries.attendence_system.databinding.ActivityTeacherRegisterBinding;

import java.util.HashMap;

public class Teacher_Register extends AppCompatActivity {
    public ActivityTeacherRegisterBinding binding;
    public FirebaseAuth auth;
    public FirebaseFirestore store;
    public LogInDatabaseHandler handler;
    public DocumentReference docreference;
    public String user_id;
    public StorageReference reference,child_reference;
    public boolean passed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        binding= DataBindingUtil.setContentView(Teacher_Register.this,R.layout.activity_teacher_register);
        auth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        handler=new LogInDatabaseHandler(Teacher_Register.this);
        binding.password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int right=2;
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=(binding.password.getRight()-binding.password.getCompoundDrawables()[right].getBounds().width()))
                    {
                        int selection=binding.password.getSelectionEnd();
                        if(passed)
                        {
                            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_off,0);
                            passed=false;
                        }
                        else
                        {
                            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_on,0);
                            passed=true;
                        }
                        binding.password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        binding.confirmPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int right=2;
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    if(event.getRawX()>=(binding.confirmPass.getRight()-binding.confirmPass.getCompoundDrawables()[right].getBounds().width()))
                    {
                        int selection=binding.confirmPass.getSelectionEnd();
                        if(passed)
                        {
                            binding.confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.confirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_off,0);
                            passed=false;
                        }
                        else
                        {
                            binding.confirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.confirmPass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_on,0);
                            passed=true;
                        }
                        binding.confirmPass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(binding.tid.getText().toString().trim()))
                {
                    binding.tid.setError("Enter Proper Teacher's Id");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.name.getText().toString().trim()))
                {
                    binding.name.setError("Enter Proper Name");
                    return;
                }
                else if(TextUtils.isEmpty(binding.phoneNo.getText().toString().trim()))
                {
                    binding.phoneNo.setError("Enter proper phone_no");
                    return ;
                }
                else if(binding.phoneNo.getText().toString().trim().length()<10)
                {
                    binding.phoneNo.setError("Phone no should be of 10 integers");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.email.getText().toString().trim()))
                {
                    binding.email.setError("Enter proper email address");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.username.getText().toString().trim()))
                {
                    binding.username.setError("Enter proper username");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.password.getText().toString().trim())||TextUtils.isEmpty(binding.confirmPass.getText().toString().trim()))
                {
                    binding.password.setError("Enter proper password");
                    binding.confirmPass.setError("Enter proper confirm_password");
                    return ;
                }
                else if(!binding.password.getText().toString().trim().equals(binding.confirmPass.getText().toString().trim()))
                {
                    binding.password.setError("Passwor and confirm password doesn't match");
                    return ;
                }
                else if(binding.password.getText().toString().trim().length()<8)
                {
                    binding.password.setError("Password Length should be greater than 8 charecters");
                    return ;
                }
                auth.createUserWithEmailAndPassword(binding.email.getText().toString().trim(),binding.password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Teacher_Register.this, "User Created Sucessfully", Toast.LENGTH_SHORT).show();
                            user_id=auth.getCurrentUser().getUid();
                             docreference=store.collection(Keys.COLLECTION_NAME).document(user_id);
                            HashMap<String,String> map = new HashMap<String,String>();
                            map.put(Keys.TID,binding.tid.getText().toString().trim());
                            map.put(Keys.NAME,binding.name.getText().toString().trim());
                            map.put(Keys.PHONE_NO,binding.phoneNo.getText().toString().trim());
                            map.put(Keys.EMAIL,binding.email.getText().toString().trim());
                            map.put(Keys.USER_TYPE,Keys.TEACHER);
                            map.put(Keys.USERNAME,binding.username.getText().toString().trim());
                            map.put(Keys.PASSWORD,binding.password.getText().toString().trim());
                            User user = new User();
                            user.setUsername(binding.tid.getText().toString().trim());
                            user.setPassword(binding.password.getText().toString().trim());
                            user.setName(binding.name.getText().toString().trim());
                            user.setUser_type(Keys.TEACHER);
                            handler.insertUser(user);
                            docreference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent inext = new Intent(Teacher_Register.this, Teachers_DashBoard.class);
                                    startActivity(inext);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Teacher_Register.this, "Email may exists", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
        binding.teachersProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,Keys.GALLERY_PROFILE_IMAGE);
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
                binding.teachersProfileImage.setImageURI(data.getData());
                reference=FirebaseStorage.getInstance().getReference();
                child_reference=reference.child(Keys.TEACHERS_PROFILE_IMAGE);
                child_reference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Teacher_Register.this, "Profile Image added Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Teacher_Register.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}