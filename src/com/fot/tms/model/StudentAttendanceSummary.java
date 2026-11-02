package com.fot.tms.model;

import java.io.Serializable;

/**
 * Summary DTO for calculating and displaying attendance scenarios.
 * Covers Theory-only, Practical-only, Combined formats, Raw vs Effective %,
 * and the 5 specific SRS attendance scenario classifications.
 */
public class StudentAttendanceSummary implements Serializable {
    private int studentId;
    private String studentRegNo;
    private String studentName;
    private int courseId;
    private String courseCode;
    private String courseName;

    private int theoryTotalSessions;
    private int theoryPresentCount;
    private int theoryApprovedMedicals;

    private int practicalTotalSessions;
    private int practicalPresentCount;
    private int practicalApprovedMedicals;

    private int combinedTotalSessions;
    private int combinedPresentCount;
    private int combinedApprovedMedicals;

    private double theoryRawPercentage;
    private double theoryEffectivePercentage;

    private double practicalRawPercentage;
    private double practicalEffectivePercentage;

    private double combinedRawPercentage;
    private double combinedEffectivePercentage;

    private String scenarioCategory; // ">80%", "80% (Exact)", "<80% (No Medical)", ">80% (With Medical)", "<80% (With Medical)"
    private boolean eligible;

    public StudentAttendanceSummary() {}

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

    public int getTheoryTotalSessions() {
        return theoryTotalSessions;
    }

    public void setTheoryTotalSessions(int theoryTotalSessions) {
        this.theoryTotalSessions = theoryTotalSessions;
    }

    public int getTheoryPresentCount() {
        return theoryPresentCount;
    }

    public void setTheoryPresentCount(int theoryPresentCount) {
        this.theoryPresentCount = theoryPresentCount;
    }

    public int getTheoryApprovedMedicals() {
        return theoryApprovedMedicals;
    }

    public void setTheoryApprovedMedicals(int theoryApprovedMedicals) {
        this.theoryApprovedMedicals = theoryApprovedMedicals;
    }

    public int getPracticalTotalSessions() {
        return practicalTotalSessions;
    }

    public void setPracticalTotalSessions(int practicalTotalSessions) {
        this.practicalTotalSessions = practicalTotalSessions;
    }

    public int getPracticalPresentCount() {
        return practicalPresentCount;
    }

    public void setPracticalPresentCount(int practicalPresentCount) {
        this.practicalPresentCount = practicalPresentCount;
    }

    public int getPracticalApprovedMedicals() {
        return practicalApprovedMedicals;
    }

    public void setPracticalApprovedMedicals(int practicalApprovedMedicals) {
        this.practicalApprovedMedicals = practicalApprovedMedicals;
    }

    public int getCombinedTotalSessions() {
        return combinedTotalSessions;
    }

    public void setCombinedTotalSessions(int combinedTotalSessions) {
        this.combinedTotalSessions = combinedTotalSessions;
    }

    public int getCombinedPresentCount() {
        return combinedPresentCount;
    }

    public void setCombinedPresentCount(int combinedPresentCount) {
        this.combinedPresentCount = combinedPresentCount;
    }

    public int getCombinedApprovedMedicals() {
        return combinedApprovedMedicals;
    }

    public void setCombinedApprovedMedicals(int combinedApprovedMedicals) {
        this.combinedApprovedMedicals = combinedApprovedMedicals;
    }

    public double getTheoryRawPercentage() {
        return theoryRawPercentage;
    }

    public void setTheoryRawPercentage(double theoryRawPercentage) {
        this.theoryRawPercentage = theoryRawPercentage;
    }

    public double getTheoryEffectivePercentage() {
        return theoryEffectivePercentage;
    }

    public void setTheoryEffectivePercentage(double theoryEffectivePercentage) {
        this.theoryEffectivePercentage = theoryEffectivePercentage;
    }

    public double getPracticalRawPercentage() {
        return practicalRawPercentage;
    }

    public void setPracticalRawPercentage(double practicalRawPercentage) {
        this.practicalRawPercentage = practicalRawPercentage;
    }

    public double getPracticalEffectivePercentage() {
        return practicalEffectivePercentage;
    }

    public void setPracticalEffectivePercentage(double practicalEffectivePercentage) {
        this.practicalEffectivePercentage = practicalEffectivePercentage;
    }

    public double getCombinedRawPercentage() {
        return combinedRawPercentage;
    }

    public void setCombinedRawPercentage(double combinedRawPercentage) {
        this.combinedRawPercentage = combinedRawPercentage;
    }

    public double getCombinedEffectivePercentage() {
        return combinedEffectivePercentage;
    }

    public void setCombinedEffectivePercentage(double combinedEffectivePercentage) {
        this.combinedEffectivePercentage = combinedEffectivePercentage;
    }

    public String getScenarioCategory() {
        return scenarioCategory != null ? scenarioCategory : "";
    }

    public void setScenarioCategory(String scenarioCategory) {
        this.scenarioCategory = scenarioCategory;
    }

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }
}
