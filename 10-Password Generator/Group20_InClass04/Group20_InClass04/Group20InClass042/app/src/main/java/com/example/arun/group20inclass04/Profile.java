package com.example.arun.group20inclass04;

/**
 * Created by Arun on 9/18/2017.
 */

public class Profile {

    String name;
    String dept;
    int age;
    int zip;

    public Profile(String name, String dept, int age, int zip) {
        this.name = name;
        this.dept = dept;
        this.age = age;
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
}
