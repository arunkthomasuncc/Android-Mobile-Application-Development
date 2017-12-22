package com.example.roncherian.homework06;

/**
 * Created by roncherian on 04/11/17.
 */

public class InstructorObj {
    String id;
    String firstName, lastName, email, website;
    boolean isSelected;
    byte[] imageArray;

    public byte[] getImageArray() {
        return imageArray;
    }

    public void setImageArray(byte[] imageArray) {
        this.imageArray = imageArray;
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
