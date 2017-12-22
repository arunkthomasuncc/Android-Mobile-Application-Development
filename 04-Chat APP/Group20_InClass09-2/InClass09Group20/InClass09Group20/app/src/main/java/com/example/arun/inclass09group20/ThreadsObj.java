package com.example.arun.inclass09group20;

/**
 * Created by Arun on 11/6/2017.
 */

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by roncherian on 06/11/17.
 */

public class ThreadsObj implements Serializable{


    String fName, lName, title, createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    int id;
    int userId;



   /*  "user_fname": "salman",
             "user_lname": "mujtaba",
             "user_id": "32",
             "id": "29",
             "title": "Salman's thread",
             "created_at": "2017-11-07 00:27:19"*/


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static Comparator<ThreadsObj> sortById= new Comparator<ThreadsObj>() {
        @Override
        public int compare(ThreadsObj obj1, ThreadsObj obj2) {
            //return t1.getRating()-(track.getRating());

            if (obj2.getId() > obj1.getId())
                return -1;
            if (obj2.getId() < obj1.getId())
                return 1;
            return 0;
        }
    };




}
