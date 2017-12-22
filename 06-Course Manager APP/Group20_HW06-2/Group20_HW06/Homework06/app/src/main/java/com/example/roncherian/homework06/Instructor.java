package com.example.roncherian.homework06;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by roncherian on 03/11/17.
 */

public class Instructor extends RealmObject {

    @PrimaryKey
    String id;
    String firstName, lastName, email, website;
    boolean isSelected;
    String username;
    byte[] imageArray;

    public byte[] getImageArray() {
        return imageArray;
    }

    public void setImageArray(byte[] imageArray) {
        this.imageArray = imageArray;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
