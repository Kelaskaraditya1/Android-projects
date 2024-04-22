package com.starkindustries.attendence_system;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.starkindustries.attendence_system.Database.LogInDatabaseHandler;
import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.databinding.ActivityLoginBinding;

import java.security.Key;

public class Login_Activity extends AppCompatActivity {
public ActivityLoginBinding binding;
public FirebaseAuth auth;
public FirebaseFirestore store;
public LogInDatabaseHandler handler;
public SharedPreferences.Editor edit;
public boolean pass_ed;
public SharedPreferences sharedPreferences;
public TextInputEditText email;
public AppCompatButton send_email,signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding= DataBindingUtil.setContentView(Login_Activity.this,R.layout.activity_login);
        sharedPreferences=getSharedPreferences(Keys.SHARED_PREFRANCE_NAME,MODE_PRIVATE);
        edit = sharedPreferences.edit();
        auth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        handler=new LogInDatabaseHandler(Login_Activity.this);
        handler.getRegisteredCount();
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
                            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_off,0);
                            pass_ed=false;
                        }
                        else
                        {
                            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibility_on,0);
                            pass_ed=true;
                        }
                        binding.password.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(),0);
//                edit.putBoolean(Keys.STATUS,true);
//                edit.apply();
                if(TextUtils.isEmpty(binding.username.getText().toString().trim()))
                {
                    binding.username.setError("Enter Proper username");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.password.getText().toString().trim()))
                {
                    binding.password.setError("Enter Proper password");
                    return ;
                }
                else if(binding.password.getText().toString().trim().length()<6)
                {
                    binding.password.setError("Password should be greater than 6 charecters");
                    return ;
                }
//                auth.signInWithEmailAndPassword(binding.username.getText().toString().trim(),binding.password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful())
//                        {
//                            edit.putBoolean(Keys.FLAG,true);
//                            edit.apply();
//                            Intent inext = new Intent(Login_Activity.this, Student_Entry_Database.class);
//                            edit.putBoolean(Keys.FLAG,true);
//                            edit.apply();
//                            startActivity(inext);
//                            finish();
//                        }
//                        else
//                        {
//                            Toast.makeText(Login_Activity.this, "Either Email or Password is incorrect", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                String user = handler.authenticateUSernamePassword(binding.username.getText().toString().trim(),binding.password.getText().toString().trim());
                if(user!=null) {
                    if(user.equals(Keys.STUDENT))
                    {
                        Intent inext = new Intent(Login_Activity.this,Student_Dashboard.class);
                        edit.putBoolean(Keys.FLAG,true);
                        edit.putString(Keys.USER_TYPE,Keys.STUDENT);
                        edit.apply();
                        startActivity(inext);
                        finish();
                    }
                    else if(user.equals(Keys.TEACHER))
                    {
                        Intent inext = new Intent(Login_Activity.this,Teachers_DashBoard.class);
                        edit.putBoolean(Keys.FLAG,true);
                        edit.putString(Keys.USER_TYPE,Keys.TEACHER);
                        edit.apply();
                        startActivity(inext);
                        finish();
                    }
                }
                else
                    Toast.makeText(Login_Activity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        });
        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog forgot_pass_dialog = new Dialog(Login_Activity.this);
                forgot_pass_dialog.setContentView(R.layout.forgot_password_dialog);
                email=forgot_pass_dialog.findViewById(R.id.reset_email);
                send_email=forgot_pass_dialog.findViewById(R.id.send_email);
                signin=forgot_pass_dialog.findViewById(R.id.signin);
                signin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forgot_pass_dialog.dismiss();
                    }
                });
                send_email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(email.getText().toString().trim()))
                        {
                            email.setError("Enter Proper Email");
                            return ;
                        }
                        auth.sendPasswordResetEmail(email.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Login_Activity.this, "Password Reset Email sent sucessfully", Toast.LENGTH_SHORT).show();
                                forgot_pass_dialog.dismiss();
                            }
                        });
                    }
                });
                forgot_pass_dialog.show();
                forgot_pass_dialog.setCancelable(false);
            }
        });
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inext = new Intent(Login_Activity.this,User_Activity.class);
                startActivity(inext);
            }
        });
    }
}