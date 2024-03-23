package com.starkindustries.contactbook;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.starkindustries.contactbook.Adapter.RecyclerViewAdapter;
import com.starkindustries.contactbook.Database.DatabaseHandler;
import com.starkindustries.contactbook.Model.Contact;
import com.starkindustries.contactbook.databinding.ActivityContactScreenBinding;

import java.util.ArrayList;
public class Contact_Screen extends AppCompatActivity {
    public ActivityContactScreenBinding binding;
    public ArrayList<Contact> contact_list;
    public AppCompatButton save,camera,gallery;
    public TextInputEditText name,last_name,number;
    public DatabaseHandler databaseHandler;
    public RecyclerView view;
    public Dialog dialog;
    public AppCompatImageView profile_image;
    public static int count;
    public static final int CAPTURE_REQUEST_CODE=101;
    public static final int GALLERY_CAPTURE_CODE=102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);
        binding= DataBindingUtil.setContentView(Contact_Screen.this,R.layout.activity_contact_screen);
        databaseHandler = new DatabaseHandler(Contact_Screen.this);
        databaseHandler.get_count();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(Contact_Screen.this));
        setSupportActionBar(binding.toolbar);
        try {
            getSupportActionBar().setTitle("Contacts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        count=0;
        contact_list=databaseHandler.get_contacts();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(Contact_Screen.this,contact_list);
        binding.recyclerView.setAdapter(adapter);
        binding.addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 dialog = new Dialog(Contact_Screen.this);
                dialog.setContentView(R.layout.add_contact_dialog);
                dialog.setCancelable(false);
                save=dialog.findViewById(R.id.save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        camera=dialog.findViewById(R.id.camera);
                        gallery=dialog.findViewById(R.id.gallery);
                        profile_image=dialog.findViewById(R.id.profile_image);
                        name = dialog.findViewById(R.id.name);
                        last_name = dialog.findViewById(R.id.last_name);
                        number = dialog.findViewById(R.id.number);
                        camera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent icamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(icamera,CAPTURE_REQUEST_CODE);
                            }
                        });
                        gallery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent igallery = new Intent(Intent.ACTION_PICK);
                                igallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(igallery,GALLERY_CAPTURE_CODE);
                            }
                        });
                        if(!(name.getText().toString().equals(""))&&!(last_name.getText().toString().equals(""))&&!(number.getText().toString().equals("")))
                        {
                            Contact contact = new Contact();
                            String full_name = name.getText().toString().trim() + " " + last_name.getText().toString().trim();
//                            contact.setId(count++);
                            contact.setName(full_name);
                            contact.setNumber(number.getText().toString().trim());
                            databaseHandler = new DatabaseHandler(Contact_Screen.this);
//                            contact_list.add(contact);
                            databaseHandler.add_contact(contact);
                            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(Contact_Screen.this, contact_list);
                            AppCompatImageView image = findViewById(R.id.contact_image);
                            image.setImageResource(R.drawable.boy_three);
                            recyclerViewAdapter.notifyItemChanged(contact_list.size() - 1);
                            binding.recyclerView.scrollToPosition(contact_list.size() - 1);
                            Log.d("Contact", "Contact " + full_name + " added Sucessfully");
                            dialog.dismiss();
                        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(view.getWindowToken(),0);
                    }
                        else
                            Toast.makeText(Contact_Screen.this, "Please enter Complete information", Toast.LENGTH_SHORT).show();
                        }
                });
                dialog.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(Contact_Screen.this).inflate(R.menu.toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.search)
            Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show();
        else if(id==android.R.id.home)
        {
            AlertDialog.Builder exit_dialog = new AlertDialog.Builder(Contact_Screen.this);
            exit_dialog.setCancelable(false);
            exit_dialog.setIcon(R.drawable.exit);
            exit_dialog.setTitle("Exit");
            exit_dialog.setMessage("Are you sure, you want to exit");
            exit_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Contact_Screen.super.onBackPressed();
                }
            });
            exit_dialog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            exit_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            exit_dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if(dialog.isShowing())
        {
            AlertDialog.Builder discard_dialog = new AlertDialog.Builder(Contact_Screen.this);
            discard_dialog.setCancelable(false);
            discard_dialog.setIcon(R.drawable.cancel);
            discard_dialog.setTitle("Contact");
            discard_dialog.setMessage("Are you sure,you don't want to create");
            discard_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Contact_Screen.super.onBackPressed();
                }
            });
            discard_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            discard_dialog.show();
        }
        AlertDialog.Builder exit_dialog = new AlertDialog.Builder(Contact_Screen.this);
        exit_dialog.setIcon(R.drawable.exit);
        exit_dialog.setTitle("Exit");
        exit_dialog.setMessage("Are you sure,you want to quit?");
        exit_dialog.setCancelable(false);
        exit_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Contact_Screen.super.onBackPressed();
            }
        });exit_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    });
        exit_dialog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        exit_dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==CAPTURE_REQUEST_CODE)
            {
                Bitmap bitmap_image = (Bitmap) data.getExtras().get("data");
                profile_image.setImageBitmap(bitmap_image);
            }
            if(requestCode==GALLERY_CAPTURE_CODE)
            {
                profile_image.setImageURI(data.getData());
            }
        }
    }
}