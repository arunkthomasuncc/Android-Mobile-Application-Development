package com.example.arun.homework07v1;

/**
 * Created by Arun on 11/17/2017.
 */

public class User {

    String userId;
    String firstName;
    String lastName;
    String email;
    String dob;

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public boolean equals(Object obj) {
        User user=(User)obj;
        return this.getUserId().equals(user.getUserId());

    }
}
