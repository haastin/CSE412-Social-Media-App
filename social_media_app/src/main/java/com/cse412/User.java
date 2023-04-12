package com.cse412;

public class User {

    public String firstName;
    public String lastName;
    public String hashPass;
    public char gender;
    public String hometown;
    public String email;
    public String dob;

    public User() {
    }

    public User(String firstName, String lastName, String hashPass, char gender, String hometown, String email,
            String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPass = hashPass;
        this.gender = gender;
        this.hometown = hometown;
        this.email = email;
        this.dob = dob;
    }

}
