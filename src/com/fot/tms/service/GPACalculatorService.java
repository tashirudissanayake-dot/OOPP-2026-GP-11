package com.fot.tms.service;

import com.fot.tms.dao.MarkDAO;
import com.fot.tms.dao.UserDAO;
import com.fot.tms.model.Mark;
import com.fot.tms.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GPACalculatorService implements IGPACalculator {
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
        private int semester = 1;
        private int semesterCredits;
        private double semesterQualityPoints;
        private double sgpa;
        private int totalCredits;
        private double totalQualityPoints;
        private double cgpa;
        private double gpa; // Backward compatible getter
        private String academicStanding;
        private List<Mark> subjectMarks = new ArrayList<>();

        public int getStudentId() { return studentId; }
        public void setStudentId(int studentId) { this.studentId = studentId; }

        public String getStudentRegNo() { return studentRegNo != null ? studentRegNo : ""; }
        public void setStudentRegNo(String studentRegNo) { this.studentRegNo = studentRegNo; }

        public String getStudentName() { return studentName != null ? studentName : ""; }
        public void setStudentName(String studentName) { this.studentName = studentName; }

        public int getSemester() { return semester; }
        public void setSemester(int semester) { this.semester = semester; }

        public int getSemesterCredits() { return semesterCredits; }
        public void setSemesterCredits(int semesterCredits) { this.semesterCredits = semesterCredits; }

        public double getSemesterQualityPoints() { return semesterQualityPoints; }
        public void setSemesterQualityPoints(double semesterQualityPoints) { this.semesterQualityPoints = semesterQualityPoints; }

        public double getSgpa() { return sgpa; }
        public void setSgpa(double sgpa) { this.sgpa = sgpa; }

        public int getTotalCredits() { return totalCredits; }
        public void setTotalCredits(int totalCredits) { this.totalCredits = totalCredits; }

        public double getTotalQualityPoints() { return totalQualityPoints; }
        public void setTotalQualityPoints(double totalQualityPoints) { this.totalQualityPoints = totalQualityPoints; }

        public double getCgpa() { return cgpa; }
        public void setCgpa(double cgpa) { this.cgpa = cgpa; }

        public double getGpa() { return cgpa > 0 ? cgpa : sgpa; }
        public void setGpa(double gpa) { this.gpa = gpa; this.cgpa = gpa; }

        public String getAcademicStanding() { return academicStanding != null ? academicStanding : "Good Standing"; }
        public void setAcademicStanding(String academicStanding) { this.academicStanding = academicStanding; }

        public List<Mark> getSubjectMarks() { return subjectMarks; }
        public void setSubjectMarks(List<Mark> subjectMarks) { this.subjectMarks = subjectMarks; }
    }

    @Override
    public StudentGPASummary calculateGPAForStudent(int studentId) {
        return calculateGPAForStudent(studentId, 1);
    }

    @Override
    public StudentGPASummary calculateGPAForStudent(int studentId, Integer targetSemester) {
        User user = userDAO.findById(studentId);
        List<Mark> marks = markDAO.findByStudentId(studentId);

        int totalCredits = 0;
        double totalQP = 0.0;

        int semCredits = 0;
        double semQP = 0.0;

        List<Mark> studentMarks = new ArrayList<>();

        for (Mark m : marks) {
            int cr = m.getCredits();
            double gp = m.getGradePoint();
            int sem = m.getSemester();

            totalCredits += cr;
            totalQP += (cr * gp);

            if (targetSemester == null || sem == targetSemester) {
                semCredits += cr;
                semQP += (cr * gp);
                studentMarks.add(m);
            }
        }

        double cgpa = totalCredits > 0 ? totalQP / totalCredits : 0.0;
        cgpa = Math.round(cgpa * 100.0) / 100.0;

        double sgpa = semCredits > 0 ? semQP / semCredits : cgpa;
        sgpa = Math.round(sgpa * 100.0) / 100.0;

        String standing;
        if (cgpa >= 3.70) standing = "First Class Honours";
        else if (cgpa >= 3.30) standing = "Second Class (Upper Division)";
        else if (cgpa >= 3.00) standing = "Second Class (Lower Division)";
        else if (cgpa >= 2.00) standing = "Pass";
        else standing = "Academic Warning / Repeat";

        StudentGPASummary summary = new StudentGPASummary();
        summary.setStudentId(studentId);
        if (user != null) {
            summary.setStudentRegNo(user.getUsername());
            summary.setStudentName(user.getFullName());
        }
        summary.setSemester(targetSemester != null ? targetSemester : 1);
        summary.setSemesterCredits(semCredits);
        summary.setSemesterQualityPoints(Math.round(semQP * 100.0) / 100.0);
        summary.setSgpa(sgpa);

        summary.setTotalCredits(totalCredits);
        summary.setTotalQualityPoints(Math.round(totalQP * 100.0) / 100.0);
        summary.setCgpa(cgpa);
        summary.setGpa(cgpa);
        summary.setAcademicStanding(standing);
        summary.setSubjectMarks(studentMarks);

        return summary;
    }

    @Override
    public List<StudentGPASummary> getBatchGPASummaries() {
        return getBatchGPASummaries(1);
    }

    @Override
    public List<StudentGPASummary> getBatchGPASummaries(Integer semester) {
        List<StudentGPASummary> list = new ArrayList<>();
        List<User> students = userDAO.findByRole("Undergraduate");
        for (User u : students) {
            list.add(calculateGPAForStudent(u.getUserId(), semester));
        }
        return list;
    }
}
