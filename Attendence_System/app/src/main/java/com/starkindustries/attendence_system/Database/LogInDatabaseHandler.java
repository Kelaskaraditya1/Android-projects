package com.starkindustries.attendence_system.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.starkindustries.attendence_system.Keys.Keys;
import com.starkindustries.attendence_system.Model.Student;
import com.starkindustries.attendence_system.Model.User;

import java.util.ArrayList;
public class LogInDatabaseHandler extends SQLiteOpenHelper {
    public ArrayList<Student> students_list;
    public ArrayList<String> sid_return_list;
    public LogInDatabaseHandler(@Nullable Context context) {
        super(context, Keys.LOGIN_DATABASE, null, Keys.VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table "+Keys.LOGIN_TABLE+"( "+Keys.USERNAME+" Text primary key , "+Keys.PASSWORD+" Text, "+Keys.NAME+" Text, "+Keys.USER_TYPE+" Text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists "+Keys.LOGIN_TABLE);
        onCreate(db);
    }
    public void create_student(Student student)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Keys.SID,student.getSid());
        content.put(Keys.NAME,student.getName());
        content.put(Keys.DEPARTMENT,student.getDepartment());
        content.put(Keys.YEAR,student.getYear());
        content.put(Keys.PHONE_NO,student.getPhone_no());
        content.put(Keys.EMAIL,student.getEmail());
        content.put(Keys.USERNAME,student.getUsername());
        db.insert(Keys.STUDENTS_INFO_TABLE,null,content);
        Log.d("student","Student "+student.getName()+"added sucessfully");
        db.close();
    }
    public void update_student(Student student,String sid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(Keys.NAME,student.getName());
        content.put(Keys.DEPARTMENT,student.getDepartment());
        content.put(Keys.YEAR,student.getYear());
        content.put(Keys.PHONE_NO,student.getPhone_no());
        content.put(Keys.EMAIL,student.getEmail());
        content.put(Keys.USERNAME,student.getUsername());
        db.update(Keys.STUDENTS_INFO_TABLE,content,Keys.SID+"=?",new String[]{sid});
        db.close();
    }
    public ArrayList<Student> get_student_list()
    {
        students_list= new ArrayList<Student>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+Keys.STUDENTS_INFO_TABLE,null);
        if(cursor!=null)
            cursor.moveToFirst();
        while(cursor.moveToNext())
        {
            Student student = new Student();
            student.setSid(cursor.getString(1));
            student.setName(cursor.getString(2));
            student.setDepartment(cursor.getString(3));
            student.setEmail(cursor.getString(6));
            student.setUsername(cursor.getString(7));
            student.setYear(cursor.getString(4));
            student.setPhone_no(cursor.getString(5));
            students_list.add(student);
        }
        return students_list;
    }
    public Student get_student(String sid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Student student = new Student();
        Cursor cursor = db.query(Keys.STUDENTS_INFO_TABLE,new String[]{Keys.NAME,Keys.DEPARTMENT,Keys.EMAIL,Keys.USERNAME,Keys.YEAR,Keys.PHONE_NO},Keys.SID+"=?",new String[]{sid},null,null,null);
        student.setName(cursor.getString(2));
        student.setDepartment(cursor.getString(3));
        student.setEmail(cursor.getString(6));
        student.setUsername(cursor.getString(7));
        student.setYear(cursor.getString(4));
        student.setPhone_no(cursor.getString(5));
        return student;
    }
    public int student_count()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+Keys.STUDENTS_INFO_TABLE,null);
        if(cursor!=null)
            cursor.moveToNext();
        return cursor.getCount();
    }
    public ArrayList<String> sid_return()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "+Keys.SID+" from "+Keys.STUDENTS_INFO_TABLE,null);
        if(cursor!=null)
            cursor.moveToFirst();
        else
            Log.d("empty","cursor is empty");
        while(cursor.moveToNext())
            sid_return_list.add(cursor.getString(1));
        return sid_return_list;
    }
    public boolean search_Query(String sid,String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select from"+Keys.STUDENTS_INFO_TABLE+"where "+Keys.SID+"="+sid+"and"+Keys.EMAIL+"="+email,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
            return true;
        }
        return false;
    }
    public void insertUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Keys.USERNAME,user.getUsername());
        values.put(Keys.PASSWORD,user.getPassword());
        values.put(Keys.NAME,user.getName());
        values.put(Keys.USER_TYPE,user.getUser_type());
        db.insert(Keys.LOGIN_TABLE,null,values);
        Log.d("insert","User added successfully "+user.getName());
    }
    public String authenticateUSernamePassword(String username,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Keys.LOGIN_TABLE,new String[]{Keys.USERNAME,Keys.PASSWORD,Keys.NAME,Keys.USER_TYPE},Keys.USERNAME+"=?"+" and "+Keys.PASSWORD+"=?",new String[]{username,password},null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        if(cursor.getCount()!=0)
        {
             return cursor.getString(3);
        }
        return null;
    }
}
