package com.fot.tms.service;

import com.fot.tms.dao.MarkDAO;
import com.fot.tms.dao.UserDAO;
import com.fot.tms.model.Mark;
import com.fot.tms.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GPACalculatorService {
    private final MarkDAO markDAO;
    private final UserDAO userDAO;

    public GPACalculatorService() {
        this.markDAO = new MarkDAO();
        this.userDAO = new UserDAO();
    }

    public static class StudentGPASummary implements Serializable {
        private int studentId;
        private String studentRegNo;
        private String studentName;
        private int totalCredits;
        private double totalQualityPoints;
        private double gpa;
        private String academicStanding;

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public String getStudentRegNo() {
            return studentRegNo;
        }

        public void setStudentRegNo(String studentRegNo) {
            this.studentRegNo = studentRegNo;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public int getTotalCredits() {
            return totalCredits;
        }

        public void setTotalCredits(int totalCredits) {
            this.totalCredits = totalCredits;
        }

        public double getTotalQualityPoints() {
            return totalQualityPoints;
        }

        public void setTotalQualityPoints(double totalQualityPoints) {
            this.totalQualityPoints = totalQualityPoints;
        }

        public double getGpa() {
            return gpa;
        }

        public void setGpa(double gpa) {
            this.gpa = gpa;
        }

        public String getAcademicStanding() {
            return academicStanding;
        }

        public void setAcademicStanding(String academicStanding) {
            this.academicStanding = academicStanding;
        }
    }

    public StudentGPASummary calculateGPAForStudent(int studentId) {
        User user = userDAO.findById(studentId);
        List<Mark> marks = markDAO.findByStudentId(studentId);

        int totalCredits = 0;
        double totalQP = 0.0;

        for (Mark m : marks) {
            int cr = m.getCredits();
            double gp = m.getGradePoint();
            totalCredits += cr;
            totalQP += (cr * gp);
        }

        double gpa = totalCredits > 0 ? totalQP / totalCredits : 0.0;
        gpa = Math.round(gpa * 100.0) / 100.0;

        String standing;
        if (gpa >= 3.70) standing = "First Class Honours";
        else if (gpa >= 3.30) standing = "Second Class (Upper Division)";
        else if (gpa >= 3.00) standing = "Second Class (Lower Division)";
        else if (gpa >= 2.00) standing = "Pass";
        else standing = "Academic Warning / Repeat";

        StudentGPASummary summary = new StudentGPASummary();
        summary.setStudentId(studentId);
        if (user != null) {
            summary.setStudentRegNo(user.getUsername());
            summary.setStudentName(user.getFullName());
        }
        summary.setTotalCredits(totalCredits);
        summary.setTotalQualityPoints(Math.round(totalQP * 100.0) / 100.0);
        summary.setGpa(gpa);
        summary.setAcademicStanding(standing);

        return summary;
    }

    public List<StudentGPASummary> getBatchGPASummaries() {
        List<StudentGPASummary> list = new ArrayList<>();
        List<User> students = userDAO.findByRole("Undergraduate");
        for (User u : students) {
            list.add(calculateGPAForStudent(u.getUserId()));
        }
        return list;
    }
}
