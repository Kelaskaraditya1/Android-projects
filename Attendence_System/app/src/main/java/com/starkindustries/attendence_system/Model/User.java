package com.starkindustries.attendence_system.Model;
public class User
{
    public String username,password,name,user_type;
    public User(String username,String password,String name,String user_type)
    {
        this.username=username;
        this.password=password;
        this.name=name;
        this.user_type=user_type;
    }
    public User(String username,String name,String user_type)
    {
        this.username=username;
        this.name=name;
        this.user_type=user_type;
    }
    public User()
    {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
