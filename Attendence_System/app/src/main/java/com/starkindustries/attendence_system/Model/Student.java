package com.starkindustries.attendence_system.Model;

public class Student
{
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String sid;
    public String name;
    public String department;
    public String year;
    public String phone_no;
    public String email;
    public String division;
    public String username;
    public Student(String sid,String name,String department,String division,String year,String phone_no,String email)
    {
        this.sid=sid;
        this.name=name;
        this.department=department;
        this.year=year;
        this.division=division;
        this.phone_no=phone_no;
        this.email=email;
    }
    public Student()
    {}
    public Student(String name,String department,String division,String year,String phone_no,String email)
    {
        this.name=name;
        this.department=department;
        this.division=division;
        this.year=year;
        this.phone_no=phone_no;
        this.email=email;
    }
}
