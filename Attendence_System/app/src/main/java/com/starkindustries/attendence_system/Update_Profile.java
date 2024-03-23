package com.starkindustries.attendence_system;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.databinding.ActivityUpdateProfileBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class Update_Profile extends AppCompatActivity {
    public ActivityUpdateProfileBinding binding;
    public ArrayList<String> year_list;
    public ArrayList<String> department_list;
    public ArrayList<String> division_list;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public FirebaseFirestore store;
    public StorageReference storageReference,child_reference;
    public DocumentReference documentReference;
    public String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        binding= DataBindingUtil.setContentView(Update_Profile.this,R.layout.activity_update_profile);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        store=FirebaseFirestore.getInstance();
        user_id=auth.getCurrentUser().getUid();
        documentReference=store.collection(Keys.COLLECTION_NAME).document(user_id);
        storageReference= FirebaseStorage.getInstance().getReference();
        child_reference=storageReference.child(Keys.STUDENT_PROFILE_IMAGE);
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
        ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(Update_Profile.this,android.R.layout.simple_list_item_1,year_list);
        binding.updateYearSpinner.setAdapter(year_adapter);
        ArrayAdapter<String> department_adapter = new ArrayAdapter<String>(Update_Profile.this, android.R.layout.simple_list_item_1,department_list);
        binding.updateDepartmentSpinner.setAdapter(department_adapter);
        ArrayAdapter<String> division_adapter = new ArrayAdapter<String>(Update_Profile.this, android.R.layout.simple_list_item_1,division_list);
        binding.updateDivisionSpinner.setAdapter(division_adapter);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists())
                {
                    binding.updateSid.setText(value.get(Keys.SID).toString());
                    binding.updateName.setText(value.get(Keys.NAME).toString());
                    binding.updatePhoneNo.setText(value.get(Keys.PHONE_NO).toString());
                    binding.updateEmail.setText(value.get(Keys.EMAIL).toString());
                    binding.updateUsername.setText(value.get(Keys.USERNAME).toString());
                }
            }
        });
//        child_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(binding.updateProfileImage);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Update_Profile.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
//            }
//        });
//        binding.updateProfileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery = new Intent(Intent.ACTION_PICK);
//                startActivityForResult(gallery,Keys.UPDATE_STUDENT_PROFILE_IMAGE);
//            }
//        });
        binding.updateProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery,Keys.UPDATE_STUDENT_PROFILE_IMAGE);
            }
        });
        binding.updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(binding.updateSid.getText().toString().trim()))
                {
                    binding.updateSid.setError("Enter Proper Student id");
                    return;
                }
                else if(TextUtils.isEmpty(binding.updateName.getText().toString().trim()))
                {
                    binding.updateName.setError("Enter proper Name");
                    return;
                }
                else if(TextUtils.isEmpty(binding.updatePhoneNo.getText().toString().trim()))
                {
                    binding.updatePhoneNo.setError("Enter proper phone no");
                    return ;
                }
                else if(binding.updatePhoneNo.getText().toString().trim().length()<10)
                {
                    binding.updatePhoneNo.setError("Phone no should be of 10 charecters");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.updateUsername.getText().toString().trim()))
                {
                    binding.updateUsername.setError("Enter Proper username");
                    return ;
                }
                else if(TextUtils.isEmpty(binding.updateEmail.getText().toString().trim()))
                {
                    binding.updateEmail.setError("Enter proper Email");
                    return ;
                }
                HashMap<String,Object> map = new HashMap<String,Object>();
                map.put(Keys.SID,binding.updateSid.getText().toString().trim());
                map.put(Keys.NAME,binding.updateName.getText().toString().trim());
                map.put(Keys.YEAR,binding.updateYearSpinner.getSelectedItem().toString().trim());
                map.put(Keys.DEPARTMENT,binding.updateDepartmentSpinner.getSelectedItem().toString().trim());
                map.put(Keys.DIVISION,binding.updateDivisionSpinner.getSelectedItem().toString().trim());
                map.put(Keys.PHONE_NO,binding.updatePhoneNo.getText().toString().trim());
                map.put(Keys.EMAIL,binding.updateEmail.getText().toString().trim());
                map.put(Keys.USERNAME,binding.updateUsername.getText().toString().trim());
                documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Update_Profile.this, "Changes saved sucessfully", Toast.LENGTH_SHORT).show();
                        Intent inext = new Intent(Update_Profile.this, Login_Activity.class);
                        startActivity(inext);
                        documentReference=store.collection(Keys.COLLECTION_NAME).document(user_id);
//                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                                if(value.exists())
//                                {
//                                    if(!user.getEmail().toString().trim().equals(binding.updateEmail.getText().toString().trim()))
//                                    {
//                                        user.updateEmail(binding.updateEmail.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Toast.makeText(Update_Profile.this, "new Email cahnged sucessfully continue login", Toast.LENGTH_SHORT).show();
//
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.d("failure", "onFailure: "+e.getMessage().toString().trim());
//                                            }
//                                        });
//
//                                    }
//
//                                    else
//                                    {
//                                        Intent inext = new Intent(Update_Profile.this, HomeScreen.class);
//                                        startActivity(inext);
//                                    }
//
//                                }
//                            }
//                        });
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
            if(requestCode==Keys.UPDATE_STUDENT_PROFILE_IMAGE)
            {
                binding.updateProfileImage.setImageURI(data.getData());
                Uri uri_image=data.getData();
                child_reference.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Update_Profile.this, "Profile Picture Updated Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Update_Profile.this, "Failed to Update Profile Image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}