package com.fot.tms.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Model representing an Attendance Record.
 */
public class Attendance implements Serializable
{
    private int attendanceId;
    private int studentId;
    private String studentRegNo;
    private String studentName;
    private int timetableId;
    private String courseCode;
    private String courseName;
    private String sessionType; // 'Theory', 'Practical'
    private Date sessionDate;
    private int sessionNumber; // 1 to 15
    private String status; // 'Present', 'Absent'
    private String medicalStatus; // 'None', 'Pending', 'Approved', 'Rejected'
    private String reason;
    public Attendance() {}

    public Attendance(int attendanceId, int studentId, int timetableId, Date sessionDate, int sessionNumber, String status, String medicalStatus, String reason)
    {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.timetableId = timetableId;
        this.sessionDate = sessionDate;
        this.sessionNumber = sessionNumber;
        this.status = status;
        this.medicalStatus = medicalStatus;
        this.reason = reason;
    }

    public int getAttendanceId()
    {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId)
    {
        this.attendanceId = attendanceId;
    }

    public int getStudentId()
    {
        return studentId;
    }

    public void setStudentId(int studentId) 
{

        this.studentId = studentId;
    }

    public String getStudentRegNo()
    {
        return studentRegNo != null ? studentRegNo : "";
    }

    public void setStudentRegNo(String studentRegNo)
    {
        this.studentRegNo = studentRegNo;
    }

    public String getStudentName()
    {
        return studentName != null ? studentName : "";
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public int getTimetableId()
    {
        return timetableId;
    }

    public void setTimetableId(int timetableId)
    {
        this.timetableId = timetableId;
    }

    public String getCourseCode()
    {
        return courseCode != null ? courseCode : "";
    }

    public void setCourseCode(String courseCode)
    {
        this.courseCode = courseCode;
    }

    public String getCourseName()
    {
        return courseName != null ? courseName : "";
    }

    public void setCourseName(String courseName)

    {
        this.courseName = courseName;
    }

    public String getSessionType()
    {
        return sessionType != null ? sessionType : "Theory";
    }

    public void setSessionType(String sessionType)
    {
        this.sessionType = sessionType;
    }

    public Date getSessionDate()
    {
        return sessionDate;
    }

    public void setSessionDate(Date sessionDate)
    {
        this.sessionDate = sessionDate;
    }

    public int getSessionNumber()
    {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber)
    {
        this.sessionNumber = sessionNumber;
    }

    public String getStatus()
    {
        return status != null ? status : "Absent";
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getMedicalStatus()
    {
        return medicalStatus != null ? medicalStatus : "None";
    }

    public void setMedicalStatus(String medicalStatus)
    {
        this.medicalStatus = medicalStatus;
    }

    public String getReason()
    {
        return reason != null ? reason : "";
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public boolean isPresentOrApprovedMedical()
    {
        return "Present".equalsIgnoreCase(status) || "Approved".equalsIgnoreCase(medicalStatus);
    }
}
