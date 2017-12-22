package com.example.roncherian.homework2;
//Ron Abraham Cherian - 801028678
//Arun Kunnumpuram Thomas
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by roncherian on 16/09/17.
 */

public class Contact implements Serializable{
    private String firstName;
    private String lastName;
    private String company;
    private String phone;
    private String email;
    private String url;
    private String address;
    private String birthday;
    private String nickname;
    private String facebookProfileUrl;
    private String twitterProfileUrl;
    private String skype;
    private String youtubeChannel;
    private byte[] contactImage;
    private boolean isImageTaken;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFacebookProfileUrl() {
        return facebookProfileUrl;
    }

    public void setFacebookProfileUrl(String facebookProfileUrl) {
        this.facebookProfileUrl = facebookProfileUrl;
    }

    public String getTwitterProfileUrl() {
        return twitterProfileUrl;
    }

    public void setTwitterProfileUrl(String twitterProfileUrl) {
        this.twitterProfileUrl = twitterProfileUrl;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getYoutubeChannel() {
        return youtubeChannel;
    }

    public void setYoutubeChannel(String youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }

    public byte[] getContactImage() {
        return contactImage;
    }

    public void setContactImage(byte[] bytes) {
        this.contactImage = bytes;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                ", nickname='" + nickname + '\'' +
                ", facebookProfileUrl='" + facebookProfileUrl + '\'' +
                ", twitterProfileUrl='" + twitterProfileUrl + '\'' +
                ", skype='" + skype + '\'' +
                ", youtubeChannel='" + youtubeChannel + '\'' +
                '}';
    }

    public boolean isImageTaken() {
        return isImageTaken;
    }

    public void setImageTaken(boolean imageTaken) {
        isImageTaken = imageTaken;
    }
}
