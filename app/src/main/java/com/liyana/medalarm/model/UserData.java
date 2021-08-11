package com.liyana.medalarm.model;

public class UserData {
    String fullname, age, email;

    public UserData(String fullname, String age, String email) {
        this.fullname = fullname;
        this.age = age;
        this.email = email;
    }

    public UserData(){

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
