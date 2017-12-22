package com.example.roncherian.homework06;

/**
 * Created by roncherian on 06/11/17.
 */

public class CourseObj {

    String id;
    InstructorObj instructor;

    String day, hours, amPm, minutes;

    String courseTitle,creditHours, semester;

    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getAmPm() {
        return amPm;
    }

    public void setAmPm(String amPm) {
        this.amPm = amPm;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InstructorObj getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorObj instructor) {
        this.instructor = instructor;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
