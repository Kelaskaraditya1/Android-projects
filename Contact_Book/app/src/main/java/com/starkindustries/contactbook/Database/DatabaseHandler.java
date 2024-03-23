package com.starkindustries.contactbook.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.starkindustries.contactbook.Keys.Keys;
import com.starkindustries.contactbook.Model.Contact;
import com.starkindustries.contactbook.R;
import java.security.Key;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static ArrayList<Contact> contact_list;
    public static int count;
    public static boolean contact_added;
    public DatabaseHandler(Context context)
    {
        super(context, Keys.DATABASE_NAME,null,Keys.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table "+Keys.TABLE_NAME+" ( "+Keys.ID+" Integer Primary Key Autoincrement, "+Keys.NAME+" Text,"+Keys.NUMBER+" Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("Drop table if exists "+Keys.TABLE_NAME);
        onCreate(db);
    }
    public void add_contact(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Keys.ID,contact.getId());
        content.put(Keys.NAME,contact.getName());
        content.put(Keys.NUMBER,contact.getNumber());
        contact_list.add(contact);
        db.insert(Keys.TABLE_NAME,null,content);
    }
    public int get_count()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+Keys.TABLE_NAME,null);
        while(cursor.moveToNext()) {
            if(cursor!=null)
                cursor.moveToFirst();
        }
        return cursor.getCount();
    }
    public ArrayList<Contact> get_contacts()
    {
        count=0;
        contact_list = new ArrayList<Contact>();
        contact_list.add(new Contact(count++, R.drawable.boy_one,"Aditya","8591059220"));
        contact_list.add(new Contact(count++,R.drawable.boy_two,"Sandeep","9819375722"));
        contact_list.add(new Contact(count++,R.drawable.boy_three,"Nidish","9654862510"));
        contact_list.add(new Contact(count++,R.drawable.chutiya_one,"Nilesh","5864654778"));
        contact_list.add(new Contact(count++,R.drawable.girl_one,"Rutika-Tai","6512457892"));
        contact_list.add(new Contact(count++,R.drawable.girl_two,"Palak","95467871235"));
        contact_list.add(new Contact(count++,R.drawable.girl_three,"Aakansha","9456312785"));
        contact_list.add(new Contact(count++,R.drawable.man_one,"Deepak","5464789812"));
        contact_list.add(new Contact(count++,R.drawable.user_logo,"Kaustubh","9819375722"));
        contact_list.add(new Contact(count++,R.drawable.user_logo,"Mayur","6545829878"));
        contact_list.add(new Contact(count++,R.drawable.boy_one,"Aditya","8591059220"));
        contact_list.add(new Contact(count++,R.drawable.boy_two,"Sandeep","9819375722"));
        contact_list.add(new Contact(count++,R.drawable.boy_three,"Nidish","9654862510"));
        contact_list.add(new Contact(count++,R.drawable.chutiya_one,"Nilesh","5864654778"));
        contact_list.add(new Contact(count++,R.drawable.girl_one,"Rutika-Tai","6512457892"));
        contact_list.add(new Contact(count++,R.drawable.girl_two,"Palak","95467871235"));
        contact_list.add(new Contact(count++,R.drawable.girl_three,"Aakansha","9456312785"));
        contact_list.add(new Contact(count++,R.drawable.man_one,"Deepak","5464789812"));
        contact_list.add(new Contact(count++,R.drawable.user_logo,"Kaustubh","9819375722"));
        contact_list.add(new Contact(count++,R.drawable.user_logo,"Mayur","6545829878"));
        return contact_list;
    }
    public void add_contacts()
    {
        int i=0;
        while(i<contact_list.size())
        {
            Contact contact=(Contact) contact_list.get(i);
            add_contact(contact);
        }
    }
    public void turn_on()
    {
        contact_added=true;
    }
    public void update_Contact(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Keys.ID,contact.getId());
        content.put(Keys.NAME,contact.getName());
        content.put(Keys.NUMBER,contact.getNumber());
        content.put(Keys.IMAGE,contact.getImage());
        db.update(Keys.TABLE_NAME,content,Keys.ID+"?=",new String[]{String.valueOf(contact.getId())});
        db.close();
    }
    public void delete_contact(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Keys.TABLE_NAME,Keys.ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }
    public Contact select_contact()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Contact contact = new Contact();
        Cursor cursor = db.query(Keys.TABLE_NAME,new String[]{Keys.ID,Keys.NAME,Keys.NUMBER,Keys.IMAGE},Keys.ID+"=?",new String[]{Keys.ID},null,null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            while(cursor!=null)
            {
                contact.setId(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setImage(cursor.getInt(3));
            }
        }
        return contact;
    }
    public ArrayList<Contact> get_contact_list()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Contact> get_contact_list;
        Cursor cursor = db.query(Keys.TABLE_NAME,new String[]{Keys.ID,Keys.NAME,Keys.NUMBER,Keys.IMAGE},Keys.ID+"=?",new String[]{Keys.ID},null,null,null);
        if(cursor!=null)
        {
            get_contact_list=new ArrayList<Contact>();
            while (cursor.moveToNext())
            {
                if(cursor!=null)
                {
                    Contact contact = new Contact();
                    contact.setId(cursor.getInt(0));
                    contact.setName(cursor.getString(1));
                    contact.setNumber(cursor.getString(2));
                    contact.setImage(cursor.getInt(3));
                    get_contact_list.add(contact);
                }
            }
        }
        return contact_list;
    }

}
