package com.fot.tms.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class CourseMaterial implements Serializable {
    private int materialId;
    private int courseId;
    private String courseCode;
    private String courseName;
    private String title;
    private String filePath;
    private Timestamp uploadedDate;

    public CourseMaterial() {}

    public CourseMaterial(int materialId, int courseId, String title, String filePath, Timestamp uploadedDate) {
        this.materialId = materialId;
        this.courseId = courseId;
        this.title = title;
        this.filePath = filePath;
        this.uploadedDate = uploadedDate;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Timestamp getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(Timestamp uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    @Override
    public String toString() {
        return title;
    }
}
