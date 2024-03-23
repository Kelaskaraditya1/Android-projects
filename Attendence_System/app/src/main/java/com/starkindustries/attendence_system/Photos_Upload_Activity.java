package com.starkindustries.attendence_system;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.storage.UploadTask;
import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.databinding.ActivityPhotosUploadBinding;
public class Photos_Upload_Activity extends AppCompatActivity {
    public ActivityPhotosUploadBinding binding;
    public AppCompatImageView image_viewer;
    public FirebaseAuth auth;
    public FirebaseUser user;
    public FirebaseFirestore store;
    public DocumentReference doc_reference;
    public StorageReference reference,child_reference_left,child_reference_right,child_reference_front;
    public String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_upload);
        binding= DataBindingUtil.setContentView(Photos_Upload_Activity.this,R.layout.activity_photos_upload);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        store=FirebaseFirestore.getInstance();
        user_id=auth.getCurrentUser().getUid();
        reference= FirebaseStorage.getInstance().getReference();
        doc_reference=store.collection(Keys.COLLECTION_NAME).document(user_id);
        doc_reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists())
                {
                    String name=value.get(Keys.NAME).toString();
                    child_reference_left=reference.child(name+"/"+auth.getCurrentUser().getUid()+"/profile_left_1.jpg");
                    child_reference_front=reference.child(name+"/"+auth.getCurrentUser().getUid()+"/profile_front_1.jpg");
                    child_reference_right=reference.child(name+"/"+auth.getCurrentUser().getUid()+"/profile_right_1.jpg");
                }
            }
        });
        binding.leftCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent left_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(left_camera,Keys.LEFT_CAMERA_REQ_CODE);
            }
        });
        binding.frontCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent front_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(front_camera,Keys.FRONT_CAMERA_REQ_CODE);
            }
        });
        binding.rightCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent right_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(right_camera,Keys.RIGHT_CAMERA_REQ_CODE);
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inext = new Intent(Photos_Upload_Activity.this, Student_Dashboard.class);
                startActivity(inext);
                finish();
            }
        });
        binding.leftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, Keys.GALLERY_REQ_CODE_LEFT);
            }
        });
        binding.frontImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, Keys.GALLERY_REQ_CODE_FRONT);
            }
        });
        binding.rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, Keys.GALLERY_REQ_CODE_RIGHT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==Keys.GALLERY_REQ_CODE_LEFT)
            {

                Uri image_left=data.getData();
                child_reference_left.putFile(image_left).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Photos_Upload_Activity.this, "Left side image uploaded sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Photos_Upload_Activity.this, "Something went wrong while uploading Left Image", Toast.LENGTH_SHORT).show();
                    }
                });
                binding.leftView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(Photos_Upload_Activity.this);
                        dialog.setContentView(R.layout.image_viewer_dialog);
                        image_viewer=dialog.findViewById(R.id.image_viewer);
                        image_viewer.setImageURI(data.getData());
                        dialog.show();
                    }
                });
            }
            if(requestCode==Keys.GALLERY_REQ_CODE_FRONT)
            {
                Uri image_front=data.getData();
                child_reference_front.putFile(image_front).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Photos_Upload_Activity.this, "Front side image uploaded sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Photos_Upload_Activity.this, "Something went wrong while uploading Front image", Toast.LENGTH_SHORT).show();
                    }
                });
                binding.frontView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(Photos_Upload_Activity.this);
                        dialog.setContentView(R.layout.image_viewer_dialog);
                        image_viewer=dialog.findViewById(R.id.image_viewer);
                        image_viewer.setImageURI(data.getData());
                        dialog.show();
                    }
                });
            }
            if(requestCode==Keys.GALLERY_REQ_CODE_RIGHT)
            {
                Uri image_right=data.getData();
                child_reference_right.putFile(image_right).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Photos_Upload_Activity.this, "Right side image uploaded sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Photos_Upload_Activity.this, "Something went wrong while uploading Right image", Toast.LENGTH_SHORT).show();
                    }
                });
                binding.rightView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(Photos_Upload_Activity.this);
                        dialog.setContentView(R.layout.image_viewer_dialog);
                        image_viewer=dialog.findViewById(R.id.image_viewer);
                        image_viewer.setImageURI(data.getData());
                        dialog.show();
                    }
                });
            }
            if(requestCode==Keys.LEFT_CAMERA_REQ_CODE)
            {
                binding.leftView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(Photos_Upload_Activity.this);
                        dialog.setContentView(R.layout.image_viewer_dialog);
                        image_viewer=dialog.findViewById(R.id.image_viewer);
                        Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                        image_viewer.setImageBitmap(bitmap);
                        dialog.show();
                    }
                });
                Uri uri_image =(Uri) data.getData();
                child_reference_left.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Photos_Upload_Activity.this, "Left Image uploaded sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Photos_Upload_Activity.this, "Failed to upload Left Image", Toast.LENGTH_SHORT).show();
                        Log.d("problem",e.getMessage());
                    }
                });
            }
            if(requestCode==Keys.FRONT_CAMERA_REQ_CODE)
            {
                binding.frontView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(Photos_Upload_Activity.this);
                        dialog.setContentView(R.layout.image_viewer_dialog);
                        image_viewer=dialog.findViewById(R.id.image_viewer);
                        Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                        image_viewer.setImageBitmap(bitmap);
                        dialog.show();
                    }
                });
                Uri uri_image = (Uri)data.getData();
                child_reference_front.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Photos_Upload_Activity.this, "Front Image uploaded sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Photos_Upload_Activity.this, "Failed to upload Front Image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(requestCode==Keys.RIGHT_CAMERA_REQ_CODE)
            {
                binding.rightView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialog = new Dialog(Photos_Upload_Activity.this);
                        dialog.setContentView(R.layout.image_viewer_dialog);
                        image_viewer=dialog.findViewById(R.id.image_viewer);
                        Bitmap bitmap =(Bitmap) data.getExtras().get("data");
                        image_viewer.setImageBitmap(bitmap);
                        dialog.show();
                    }
                });
                Uri uri_image =(Uri) data.getData();
                child_reference_right.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(Photos_Upload_Activity.this, "Right Image uploaded sucessfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Photos_Upload_Activity.this, "Failed to upload Right Image", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}