package com.fot.tms.model;

import java.io.Serializable;

/**
 * Summary DTO for Combined Exam Eligibility and Academic Standing.
 * Combines Attendance criteria (>= 80%) + CA marks criteria (>= 40%).
 */
public class StudentEligibilitySummary implements Serializable {
    private int studentId;
    private String studentRegNo;
    private String studentName;
    private int courseId;
    private String courseCode;
    private String courseName;
    private int credits;

    private double effectiveAttendancePercentage;
    private boolean attendanceEligible; // >= 80%

    private double caTotal;
    private boolean caEligible; // >= 40%

    private boolean finalExamEligible; // Attendance >= 80% AND CA >= 40%

    private double finalExamMark;
    private double overallMark;
    private String letterGrade;
    private double gradePoint;

    public StudentEligibilitySummary() {}

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentRegNo() {
        return studentRegNo != null ? studentRegNo : "";
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public String getStudentName() {
        return studentName != null ? studentName : "";
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public int getCredits() {
        return credits > 0 ? credits : 3;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public double getEffectiveAttendancePercentage() {
        return effectiveAttendancePercentage;
    }

    public void setEffectiveAttendancePercentage(double effectiveAttendancePercentage) {
        this.effectiveAttendancePercentage = effectiveAttendancePercentage;
    }

    public boolean isAttendanceEligible() {
        return attendanceEligible;
    }

    public void setAttendanceEligible(boolean attendanceEligible) {
        this.attendanceEligible = attendanceEligible;
    }

    public double getCaTotal() {
        return caTotal;
    }

    public void setCaTotal(double caTotal) {
        this.caTotal = caTotal;
    }

    public boolean isCaEligible() {
        return caEligible;
    }

    public void setCaEligible(boolean caEligible) {
        this.caEligible = caEligible;
    }

    public boolean isFinalExamEligible() {
        return finalExamEligible;
    }

    public void setFinalExamEligible(boolean finalExamEligible) {
        this.finalExamEligible = finalExamEligible;
    }

    public double getFinalExamMark() {
        return finalExamMark;
    }

    public void setFinalExamMark(double finalExamMark) {
        this.finalExamMark = finalExamMark;
    }

    public double getOverallMark() {
        return overallMark;
    }

    public void setOverallMark(double overallMark) {
        this.overallMark = overallMark;
    }

    public String getLetterGrade() {
        return letterGrade != null ? letterGrade : "E";
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }
}
