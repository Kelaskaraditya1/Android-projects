package com.starkindustries.attendence_system;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.squareup.picasso.Picasso;
import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.databinding.ActivityStudentProfileBinding;
public class Student_Profile extends AppCompatActivity {
    public ActivityStudentProfileBinding binding;
    public FirebaseAuth auth;
    public FirebaseFirestore store;
    public StorageReference storageReference,child_profile_reference;
    public DocumentReference documentReference;
    public String user_id;
    public FirebaseUser current_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        binding= DataBindingUtil.setContentView(Student_Profile.this,R.layout.activity_student_profile);
        auth=FirebaseAuth.getInstance();
        store=FirebaseFirestore.getInstance();
        user_id=auth.getCurrentUser().getUid();
        documentReference=store.collection(Keys.COLLECTION_NAME).document(user_id);
        storageReference= FirebaseStorage.getInstance().getReference();

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists())
                {
                    binding.profileUsername.setText(value.get(Keys.USERNAME).toString());
                    binding.profileSid.setText(value.get(Keys.SID).toString());
                    binding.profileName.setText(value.get(Keys.NAME).toString());
                    binding.profileEmail.setText(value.get(Keys.EMAIL).toString());
                    binding.profileYear.setText(value.get(Keys.YEAR).toString());
                    binding.profileDepartment.setText(value.get(Keys.DEPARTMENT).toString());
                    binding.profileDivision.setText(value.get(Keys.DIVISION).toString());
                }
            }
        });
        binding.updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inext = new Intent(Student_Profile.this, Update_Profile.class);
                startActivity(inext);
                finish();
            }
        });
        try {
            child_profile_reference=storageReference.child(Keys.STUDENT_PROFILE_IMAGE);
            child_profile_reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(binding.profileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Student_Profile.this, "Failed to load Profile image\ncheck your internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            Log.d("failure",e.getMessage());
        }
    }
}