package com.starkindustries.attendence_system.Model;
public class Teacher {
    public String tid,name,department,email,phone_no;
    public Teacher(String tid,String name,String department,String email,String phone_no)
    {
        this.tid=tid;
        this.name=name;
        this.department=department;
        this.email=email;
        this.phone_no=phone_no;
    }
    public Teacher(String name,String department,String email,String phone_no)
    {
        this.name=name;
        this.department=department;
        this.email=email;
        this.phone_no=phone_no;
    }
    public Teacher()
    {}

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }
}
