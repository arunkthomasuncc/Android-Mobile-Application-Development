package com.example.arun.inclass03group20;
//Ron Abraham CHerian 801028678
//Arun Kunnumpuram Thomas
//INClass03Group20.zip
import java.io.Serializable;

/**
 * Created by Arun on 9/11/2017.
 */

public class Person implements Serializable{

    String name;
    String email;
    String department;
    String mood;
    int avatarType;
    int moodType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(int avatarType) {
        this.avatarType = avatarType;
    }

    public int getMoodType() {
        return moodType;
    }

    public void setMoodType(int moodType) {
        this.moodType = moodType;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", mood='" + mood + '\'' +
                ", avatarType=" + avatarType +
                ", moodType=" + moodType +
                '}';
    }
}
