package com.fot.tms.model;

import java.io.Serializable;

public class Course implements Serializable {
    private int courseId;
    private Integer coordinatorId;
    private String coordinatorName;
    private String courseCode;
    private String courseName;
    private int credits;
    private int semester;
    private double caWeight;
    private double finalWeight;

    public Course() {}

    public Course(int courseId, Integer coordinatorId, String courseCode, String courseName, int credits, int semester, double caWeight, double finalWeight) {
        this.courseId = courseId;
        this.coordinatorId = coordinatorId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
        this.caWeight = caWeight;
        this.finalWeight = finalWeight;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Integer getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Integer coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public String getCoordinatorName() {
        return coordinatorName != null ? coordinatorName : "Not Assigned";
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getCaWeight() {
        return caWeight;
    }

    public void setCaWeight(double caWeight) {
        this.caWeight = caWeight;
    }

    public double getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(double finalWeight) {
        this.finalWeight = finalWeight;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName + " (" + credits + " Credits)";
    }
}
