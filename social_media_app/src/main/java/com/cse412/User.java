package com.cse412;

public class User {

    public String firstName;
    public String lastName;
    public String hashPass;
    public String gender;
    public String hometown;
    public String email;
    public String dob;
    public int uid;

    public User() {
    }

    public User(String firstName, String lastName, String hashPass, String gender, String hometown, String email,
            String dob, int uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPass = hashPass;
        this.gender = gender;
        this.hometown = hometown;
        this.email = email;
        this.dob = dob;
        this.uid = uid;
    }

}
