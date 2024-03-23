package com.starkindustries.contactbook.Adapter;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.starkindustries.contactbook.Model.Contact;
import com.starkindustries.contactbook.R;
import java.util.ArrayList;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    public Context context;
    public  ArrayList<Contact> contact_list;
    public RecyclerViewAdapter(Context context,ArrayList<Contact> contact_list)
    {
        this.context=context;
        this.contact_list=contact_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemview = LayoutInflater.from(context).inflate(R.layout.contact_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemview);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.contact_image.setImageResource(contact_list.get(holder.getAdapterPosition()).image);
        holder.contact_name.setText(contact_list.get(holder.getAdapterPosition()).getName());
        holder.contact_number.setText(contact_list.get(holder.getAdapterPosition()).getNumber());
        holder.contact_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.call_icon.setAlpha(1f);
                holder.call_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.call_icon.setAlpha(0f);
                        Intent inext = new Intent(Intent.ACTION_DIAL);
                        inext.setData(Uri.parse("tel:"+contact_list.get(holder.getAdapterPosition()).getNumber()));
                        context.startActivity(inext);
                    }
                });
            }
        });

        holder.contact_row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder delete = new AlertDialog.Builder(context);
                delete.setIcon(R.drawable.delete);
                delete.setMessage("Are you sure,you want to delete?");
                delete.setTitle("Delete");
                delete.setCancelable(false);
                delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("No","Operation cancelled");
                    }
                });
                delete.show();
                return true;
            }
        });
        holder.contact_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.call_icon.getAlpha()==1f)
                    holder.call_icon.setAlpha(0f);
                AlertDialog.Builder update_contact = new AlertDialog.Builder(context);
                update_contact.setIcon(R.drawable.update);
                update_contact.setTitle("Update");
                update_contact.setMessage("Are you sure,you want to update");
                update_contact.setCancelable(false);
                update_contact.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextInputEditText name,last_name,number;
                        AppCompatButton update;
                        Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.add_contact_dialog);
                        name=dialog.findViewById(R.id.name);
                        number=dialog.findViewById(R.id.number);
                        last_name=dialog.findViewById(R.id.last_name);
                        update=dialog.findViewById(R.id.save);
                        update.setText("Update");
                        name.setText(contact_list.get(holder.getAdapterPosition()).getName());
                        number.setText(contact_list.get(holder.getAdapterPosition()).getNumber());
                        dialog.show();
                        dialog.setCancelable(false);
                        update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(!(name.getText().toString().equals(""))&&!(last_name.getText().toString().equals(""))&&!(number.getText().toString().equals("")))
                                {
                                    Contact contact = new Contact();
                                    String full_name=name.getText().toString().trim()+" "+last_name.getText().toString().trim();
                                    contact.setName(full_name);
                                    contact.setNumber(number.getText().toString().trim());
                                    contact_list.set(holder.getAdapterPosition(),contact);
                                    notifyItemChanged(holder.getAdapterPosition());
                                    InputMethodManager manager=getSystemService(context, InputMethodManager.class);
                                    manager.hideSoftInputFromWindow(view.getWindowToken(),0);
                                    dialog.dismiss();
                                }else
                                    Toast.makeText(context, "Please Enter Complete Information", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                update_contact.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                update_contact.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contact_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public AppCompatImageView contact_image,call_icon;
        public AppCompatTextView contact_name,contact_number;
        public LinearLayoutCompat contact_row;
        public ViewHolder(View itemview)
        {
            super(itemview);
            contact_image = itemview.findViewById(R.id.contact_image);
            contact_name=itemview.findViewById(R.id.contact_name);
            contact_number=itemview.findViewById(R.id.contact_number);
            contact_row=itemview.findViewById(R.id.contact_row);
            call_icon=itemview.findViewById(R.id.call_icon);
        }
    }

}
