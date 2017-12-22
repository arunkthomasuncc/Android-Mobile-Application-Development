package com.example.roncherian.inclass11;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

/**
 * Created by roncherian on 20/11/17.
 */

public class User implements Serializable{
    String firstName, lastName, email, imageURI;
    String userId;

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    String userUrl;

    public User() {
    }

    @Exclude
    boolean isGoogleUser = false;

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

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isGoogleUser() {
        return isGoogleUser;
    }

    public void setGoogleUser(boolean googleUser) {
        isGoogleUser = googleUser;
    }
}
