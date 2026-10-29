package com.fot.tms.service;

import com.fot.tms.dao.AttendanceDAO;
import com.fot.tms.dao.CourseDAO;
import com.fot.tms.dao.UserDAO;
import com.fot.tms.model.Attendance;
import com.fot.tms.model.Course;
import com.fot.tms.model.StudentAttendanceSummary;
import com.fot.tms.model.User;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AttendanceService
{
    private final AttendanceDAO attendanceDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;

    public AttendanceService()
    {
        this.attendanceDAO = new AttendanceDAO();
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
    }

    public boolean recordSessionAttendance(int studentId, int timetableId, Date sessionDate, int sessionNumber, String status)
    {
        return attendanceDAO.recordOrUpdateAttendance(studentId, timetableId, sessionDate, sessionNumber, status);
    }

    public boolean submitMedical(int studentId, int timetableId, int sessionNumber, String reason)
    {
        return attendanceDAO.logMedicalSubmission(studentId, timetableId, sessionNumber, reason);
    }

    public boolean reviewMedical(int attendanceId, boolean approve)
    {
        String status = approve ? "Approved" : "Rejected";
        return attendanceDAO.updateMedicalStatus(attendanceId, status);
    }

    public List<Attendance> getPendingMedicals()
    {
        return attendanceDAO.getPendingMedicals();
    }

    public List<Attendance> getAllMedicals()
    {
        return attendanceDAO.getAllMedicals();
    }

    public List<Attendance> getStudentAttendanceHistory(int studentId, Integer courseId)
    {
        return attendanceDAO.getAttendanceForStudent(studentId, courseId);
    }

    public List<Attendance> getAttendanceForSession(int timetableId, int sessionNumber)
    {
        return attendanceDAO.getAttendanceByTimetableAndSession(timetableId, sessionNumber);
    }

    public StudentAttendanceSummary calculateStudentAttendanceSummary(int studentId, int courseId)
    {
        User student = userDAO.findById(studentId);
        Course course = courseDAO.findById(courseId);
        List<Attendance> records = attendanceDAO.getAttendanceForStudent(studentId, courseId);

        StudentAttendanceSummary summary = new StudentAttendanceSummary();
        summary.setStudentId(studentId);
        if (student != null)
        {
            summary.setStudentRegNo(student.getUsername());
            summary.setStudentName(student.getFullName());
        }
        if (course != null)
        {
            summary.setCourseId(courseId);
            summary.setCourseCode(course.getCourseCode());
            summary.setCourseName(course.getCourseName());
        }

        int thTotal = 0, thPres = 0, thMed = 0;
        int prTotal = 0, prPres = 0, prMed = 0;

        for (Attendance a : records)
        {
            boolean isTheory = "Theory".equalsIgnoreCase(a.getSessionType());
            boolean isPresent = "Present".equalsIgnoreCase(a.getStatus());
            boolean isApprovedMedical = "Approved".equalsIgnoreCase(a.getMedicalStatus());

            if (isTheory)
            {
                thTotal++;
                if (isPresent) thPres++;
                if (isApprovedMedical) thMed++;
            }
            else
            {
                prTotal++;
                if (isPresent) prPres++;
                if (isApprovedMedical) prMed++;
            }
        }

        int actualRecorded = records.size();
        if (actualRecorded > 0)
        {

            if (thTotal == 0 && prTotal > 0)
            {
                thTotal = 0;
            } else if (thTotal > 0 && prTotal == 0)
            {
                prTotal = 0;
            }
        }
        else
        {

            thTotal = 15;
            prTotal = 15;
        }

        summary.setTheoryTotalSessions(thTotal);
        summary.setTheoryPresentCount(thPres);
        summary.setTheoryApprovedMedicals(thMed);

        summary.setPracticalTotalSessions(prTotal);
        summary.setPracticalPresentCount(prPres);
        summary.setPracticalApprovedMedicals(prMed);

        int combTotal = thTotal + prTotal;
        if (combTotal == 0) combTotal = 30;
        int combPres = thPres + prPres;
        int combMed = thMed + prMed;

        summary.setCombinedTotalSessions(combTotal);
        summary.setCombinedPresentCount(combPres);
        summary.setCombinedApprovedMedicals(combMed);

        double thRaw = thTotal > 0 ? ((double) thPres / thTotal) * 100.0 : 0.0;
        double thEff = thTotal > 0 ? ((double) (thPres + thMed) / thTotal) * 100.0 : 0.0;
        if (thEff > 100.0) thEff = 100.0;

        double prRaw = prTotal > 0 ? ((double) prPres / prTotal) * 100.0 : 0.0;
        double prEff = prTotal > 0 ? ((double) (prPres + prMed) / prTotal) * 100.0 : 0.0;
        if (prEff > 100.0) prEff = 100.0;

        double combRaw = combTotal > 0 ? ((double) combPres / combTotal) * 100.0 : 0.0;
        double combEff = combTotal > 0 ? ((double) (combPres + combMed) / combTotal) * 100.0 : 0.0;
        if (combEff > 100.0) combEff = 100.0;

        summary.setTheoryRawPercentage(round2(thRaw));
        summary.setTheoryEffectivePercentage(round2(thEff));
        summary.setPracticalRawPercentage(round2(prRaw));
        summary.setPracticalEffectivePercentage(round2(prEff));
        summary.setCombinedRawPercentage(round2(combRaw));
        summary.setCombinedEffectivePercentage(round2(combEff));

        boolean eligible = combEff >= 80.0;
        summary.setEligible(eligible);

        // Determine Scenario Classification
        String scenario;
        if (Math.abs(combEff - 80.0) < 0.01)
        {
            scenario = "Exactly 80% Attendance";
        }
        else if (combRaw > 80.0)
        {
            scenario = ">80% Attendance (Raw)";
        }
        else if (combEff >= 80.0 && combMed > 0)
        {
            scenario = ">80% Attendance with Medicals";
        }
        else if (combEff < 80.0 && combMed > 0)
        {
            scenario = "<80% Attendance with Medicals";
        }
        else
        {
            scenario = "<80% Attendance without Medicals";
        }
        summary.setScenarioCategory(scenario);

        return summary;
    }

    public List<StudentAttendanceSummary> getBatchAttendanceSummaries(int courseId)
    {
        List<StudentAttendanceSummary> list = new ArrayList<>();
        List<User> students = userDAO.findByRole("Undergraduate");
        for (User u : students)
        {
            list.add(calculateStudentAttendanceSummary(u.getUserId(), courseId));
        }
        return list;
    }

    private double round2(double val)
    {
        return Math.round(val * 100.0) / 100.0;
    }
}
