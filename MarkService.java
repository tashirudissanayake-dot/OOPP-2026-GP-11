package com.fot.tms.service;

import com.fot.tms.dao.CourseDAO;
import com.fot.tms.dao.MarkDAO;
import com.fot.tms.dao.UserDAO;
import com.fot.tms.model.Course;
import com.fot.tms.model.Mark;
import com.fot.tms.model.StudentAttendanceSummary;
import com.fot.tms.model.StudentEligibilitySummary;
import com.fot.tms.model.User;

import java.util.ArrayList;
import java.util.List;


public class MarkService {
    private final MarkDAO markDAO;
    private final CourseDAO courseDAO;
    private final UserDAO userDAO;
    private final AttendanceService attendanceService;

    public MarkService() {
        this.markDAO = new MarkDAO();
        this.courseDAO = new CourseDAO();
        this.userDAO = new UserDAO();
        this.attendanceService = new AttendanceService();
    }

    public static class GradeInfo {
        public final String grade;
        public final double gradePoint;

        public GradeInfo(String grade, double gradePoint) {
            this.grade = grade;
            this.gradePoint = gradePoint;
        }
    }

    public static GradeInfo calculateUGCGrade(double overallMark) {
        double mark = Math.round(overallMark * 100.0) / 100.0;
        if (mark >= 85.0) return new GradeInfo("A+", 4.00);
        if (mark >= 75.0) return new GradeInfo("A",  4.00);
        if (mark >= 70.0) return new GradeInfo("A-", 3.70);
        if (mark >= 65.0) return new GradeInfo("B+", 3.30);
        if (mark >= 60.0) return new GradeInfo("B",  3.00);
        if (mark >= 55.0) return new GradeInfo("B-", 2.70);
        if (mark >= 50.0) return new GradeInfo("C+", 2.30);
        if (mark >= 45.0) return new GradeInfo("C",  2.00);
        if (mark >= 40.0) return new GradeInfo("C-", 1.70);
        if (mark >= 35.0) return new GradeInfo("D+", 1.30);
        if (mark >= 30.0) return new GradeInfo("D",  1.00);
        return new GradeInfo("E", 0.00);
    }

    public boolean saveOrUpdateMarks(int studentId, int courseId, double caQuiz, double caAssessment, double caMid, double finalExamMark) {
        Course course = courseDAO.findById(courseId);
        double caWeight = course != null ? course.getCaWeight() : 40.0;
        double finalWeight = course != null ? course.getFinalWeight() : 60.0;

        
        double caTotal = (caQuiz + caAssessment + caMid) / 3.0;
        caTotal = Math.round(caTotal * 100.0) / 100.0;

    
        double overallMark = ((caTotal * (caWeight / 100.0)) + (finalExamMark * (finalWeight / 100.0)));
        overallMark = Math.round(overallMark * 100.0) / 100.0;

        GradeInfo gi = calculateUGCGrade(overallMark);

        Mark mark = new Mark();
        mark.setStudentId(studentId);
        mark.setCourseId(courseId);
        mark.setCaQuiz(caQuiz);
        mark.setCaAssessment(caAssessment);
        mark.setCaMid(caMid);
        mark.setCaTotal(caTotal);
        mark.setFinalExamMark(finalExamMark);
        mark.setOverallMark(overallMark);
        mark.setLetterGrade(gi.grade);
        mark.setGradePoint(gi.gradePoint);

        return markDAO.recordOrUpdateMark(mark);
    }

    public List<Mark> getMarksForStudent(int studentId) {
        return markDAO.findByStudentId(studentId);
    }

    public List<Mark> getMarksForCourse(int courseId) {
        return markDAO.findByCourseId(courseId);
    }

    public List<Mark> getAllMarks() {
        return markDAO.findAll();
    }

    public StudentEligibilitySummary getStudentEligibility(int studentId, int courseId) {
        User student = userDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        StudentAttendanceSummary attSummary = attendanceService.calculateStudentAttendanceSummary(studentId, courseId);

        List<Mark> marks = markDAO.findByStudentId(studentId);
        Mark currentMark = null;
        for (Mark m : marks) {
            if (m.getCourseId() == courseId) {
                currentMark = m;
                break;
            }
        }

        StudentEligibilitySummary summary = new StudentEligibilitySummary();
        summary.setStudentId(studentId);
        if (student != null) {
            summary.setStudentRegNo(student.getUsername());
            summary.setStudentName(student.getFullName());
        }
        if (course != null) {
            summary.setCourseId(courseId);
            summary.setCourseCode(course.getCourseCode());
            summary.setCourseName(course.getCourseName());
            summary.setCredits(course.getCredits());
        }

        double effAtt = attSummary.getCombinedEffectivePercentage();
        summary.setEffectiveAttendancePercentage(effAtt);
        boolean attEligible = effAtt >= 80.0;
        summary.setAttendanceEligible(attEligible);

        double caTot = currentMark != null ? currentMark.getCaTotal() : 0.0;
        summary.setCaTotal(caTot);
        boolean caEligible = caTot >= 40.0;
        summary.setCaEligible(caEligible);

    
        boolean finalEligible = attEligible && caEligible;
        summary.setFinalExamEligible(finalEligible);

        if (currentMark != null) {
            summary.setFinalExamMark(currentMark.getFinalExamMark());
            summary.setOverallMark(currentMark.getOverallMark());
            summary.setLetterGrade(currentMark.getLetterGrade());
            summary.setGradePoint(currentMark.getGradePoint());
        } else {
            summary.setLetterGrade("N/A");
            summary.setGradePoint(0.0);
        }

        return summary;
    }

    public List<StudentEligibilitySummary> getBatchEligibility(int courseId) {
        List<StudentEligibilitySummary> list = new ArrayList<>();
        List<User> students = userDAO.findByRole("Undergraduate");
        for (User u : students) {
            list.add(getStudentEligibility(u.getUserId(), courseId));
        }
        return list;
    }
}
