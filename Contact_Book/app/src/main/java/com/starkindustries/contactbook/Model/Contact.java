package com.starkindustries.contactbook.Model;

public class Contact
{
 public int image,id;
 public String name,number;
 public Contact(int id,int image,String name,String number)
 {
     this.id=id;
     this.image=image;
     this.name=name;
     this.number=number;
 }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setId(int id) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Contact(int id,String name, String number)
 {
     this.id=id;
     this.name=name;
     this.number=number;
 }
 public Contact(String name,String number)
 {
     this.name=name;
     this.number=number;
 }

 public Contact()
 {}
}
