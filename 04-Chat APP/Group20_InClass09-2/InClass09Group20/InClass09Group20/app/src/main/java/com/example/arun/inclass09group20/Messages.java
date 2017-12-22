package com.example.arun.inclass09group20;

/**
 * Created by Arun on 11/6/2017.
 */

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by roncherian on 06/11/17.
 */

public class Messages implements Serializable {

    String fName, lName, userId, id, message, createdAt;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static Comparator<Messages> sortById= new Comparator<Messages>() {
        @Override
        public int compare(Messages obj1, Messages obj2) {
            //return t1.getRating()-(track.getRating());

            if (Integer.parseInt(obj2.getId()) > Integer.parseInt(obj1.getId()))
                return -1;
            if (Integer.parseInt(obj2.getId()) < Integer.parseInt(obj1.getId()))
                return 1;
            return 0;
        }
    };
}
