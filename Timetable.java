package com.fot.tms.model;

import java.io.Serializable;

public class Timetable implements Serializable {
    private int timetableId;
    private int courseId;
    private String courseCode;
    private String courseName;
    private String sessionType; 
    private String dayOfWeek;   
    private String timeSlot;    
    private String venue;       
    private String department;

    public Timetable() {}

    public Timetable(int timetableId, int courseId, String sessionType, String dayOfWeek, String timeSlot, String venue, String department) {
        this.timetableId = timetableId;
        this.courseId = courseId;
        this.sessionType = sessionType;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
        this.venue = venue;
        this.department = department;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode != null ? courseCode : "";
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName != null ? courseName : "";
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return courseCode + " (" + sessionType + ") - " + dayOfWeek + " " + timeSlot + " [" + venue + "]";
    }
}
