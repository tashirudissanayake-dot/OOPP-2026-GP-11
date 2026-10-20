package com.fot.tms.model;

import java.io.Serializable;


public class Mark implements Serializable {
    private int markId;
    private int studentId;
    private String studentRegNo;
    private String studentName;
    private int courseId;
    private String courseCode;
    private String courseName;
    private int credits;
    private double caQuiz;
    private double caAssessment;
    private double caMid;
    private double caTotal;
    private double finalExamMark;
    private double overallMark;
    private String letterGrade;
    private double gradePoint;

    public Mark() {}

    public Mark(int markId, int studentId, int courseId, double caQuiz, double caAssessment, double caMid, double caTotal, double finalExamMark, double overallMark, String letterGrade, double gradePoint) {
        this.markId = markId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.caQuiz = caQuiz;
        this.caAssessment = caAssessment;
        this.caMid = caMid;
        this.caTotal = caTotal;
        this.finalExamMark = finalExamMark;
        this.overallMark = overallMark;
        this.letterGrade = letterGrade;
        this.gradePoint = gradePoint;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

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

    public double getCaQuiz() {
        return caQuiz;
    }

    public void setCaQuiz(double caQuiz) {
        this.caQuiz = caQuiz;
    }

    public double getCaAssessment() {
        return caAssessment;
    }

    public void setCaAssessment(double caAssessment) {
        this.caAssessment = caAssessment;
    }

    public double getCaMid() {
        return caMid;
    }

    public void setCaMid(double caMid) {
        this.caMid = caMid;
    }

    public double getCaTotal() {
        return caTotal;
    }

    public void setCaTotal(double caTotal) {
        this.caTotal = caTotal;
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

    public boolean isCaEligible() {
        return caTotal >= 40.0;
    }
}
