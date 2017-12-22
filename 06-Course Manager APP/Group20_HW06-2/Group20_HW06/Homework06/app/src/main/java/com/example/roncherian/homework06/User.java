package com.example.roncherian.homework06;

import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by roncherian on 03/11/17.
 */

public class User extends RealmObject {

    @PrimaryKey
    String username;
    String password;
    String firstName,lastName;
    RealmList<Courses>courses;
    byte[] imageArray;

    public byte[] getImageArray() {
        return imageArray;
    }

    public void setImageArray(byte[] imageArray) {
        this.imageArray = imageArray;
    }

    public RealmList<Courses> getCourses() {
        return courses;
    }

    public void setCourses(RealmList<Courses> courses) {
        this.courses = courses;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
