package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.Attendance;
import com.fot.tms.model.StudentAttendanceSummary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Attendance and Medical Management (FR-11 to FR-16).
 */
public class AttendanceDAO {

    public boolean recordOrUpdateAttendance(int studentId, int timetableId, Date sessionDate, int sessionNumber, String status) {
        String checkSql = "SELECT attendance_id, medical_status FROM attendance WHERE student_id = ? AND timetable_id = ? AND session_number = ?";
        String updateSql = "UPDATE attendance SET status = ?, session_date = ? WHERE student_id = ? AND timetable_id = ? AND session_number = ?";
        String insertSql = "INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason) VALUES (?, ?, ?, ?, ?, 'None', '')";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            boolean exists = false;
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setInt(1, studentId);
                check.setInt(2, timetableId);
                check.setInt(3, sessionNumber);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                try (PreparedStatement upd = conn.prepareStatement(updateSql)) {
                    upd.setString(1, status);
                    upd.setDate(2, sessionDate);
                    upd.setInt(3, studentId);
                    upd.setInt(4, timetableId);
                    upd.setInt(5, sessionNumber);
                    return upd.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                    ins.setInt(1, studentId);
                    ins.setInt(2, timetableId);
                    ins.setDate(3, sessionDate);
                    ins.setInt(4, sessionNumber);
                    ins.setString(5, status);
                    return ins.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("AttendanceDAO recordOrUpdateAttendance error: " + e.getMessage());
            return false;
        }
    }

    public boolean logMedicalSubmission(int studentId, int timetableId, int sessionNumber, String reason) {
        String checkSql = "SELECT attendance_id FROM attendance WHERE student_id = ? AND timetable_id = ? AND session_number = ?";
        String updateSql = "UPDATE attendance SET status = 'Absent', medical_status = 'Pending', reason = ? WHERE student_id = ? AND timetable_id = ? AND session_number = ?";
        String insertSql = "INSERT INTO attendance (student_id, timetable_id, session_date, session_number, status, medical_status, reason) VALUES (?, ?, CURDATE(), ?, 'Absent', 'Pending', ?)";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            boolean exists = false;
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setInt(1, studentId);
                check.setInt(2, timetableId);
                check.setInt(3, sessionNumber);
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                try (PreparedStatement upd = conn.prepareStatement(updateSql)) {
                    upd.setString(1, reason);
                    upd.setInt(2, studentId);
                    upd.setInt(3, timetableId);
                    upd.setInt(4, sessionNumber);
                    return upd.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                    ins.setInt(1, studentId);
                    ins.setInt(2, timetableId);
                    ins.setInt(3, sessionNumber);
                    ins.setString(4, reason);
                    return ins.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("AttendanceDAO logMedicalSubmission error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMedicalStatus(int attendanceId, String newStatus) {
        String sql = "UPDATE attendance SET medical_status = ? WHERE attendance_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, attendanceId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AttendanceDAO updateMedicalStatus error: " + e.getMessage());
            return false;
        }
    }

    public List<Attendance> getPendingMedicals() {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.attendance_id, a.student_id, a.timetable_id, a.session_date, a.session_number, a.status, a.medical_status, a.reason, " +
                     "u.username AS student_reg_no, CONCAT(p.first_name, ' ', p.last_name) AS student_name, " +
                     "c.course_code, c.course_name, t.session_type " +
                     "FROM attendance a " +
                     "JOIN users u ON a.student_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "JOIN timetables t ON a.timetable_id = t.timetable_id " +
                     "JOIN courses c ON t.course_id = c.course_id " +
                     "WHERE a.medical_status = 'Pending' " +
                     "ORDER BY a.session_date DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapAttendance(rs));
            }
        } catch (SQLException e) {
            System.err.println("AttendanceDAO getPendingMedicals error: " + e.getMessage());
        }
        return list;
    }

    public List<Attendance> getAllMedicals() {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.attendance_id, a.student_id, a.timetable_id, a.session_date, a.session_number, a.status, a.medical_status, a.reason, " +
                     "u.username AS student_reg_no, CONCAT(p.first_name, ' ', p.last_name) AS student_name, " +
                     "c.course_code, c.course_name, t.session_type " +
                     "FROM attendance a " +
                     "JOIN users u ON a.student_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "JOIN timetables t ON a.timetable_id = t.timetable_id " +
                     "JOIN courses c ON t.course_id = c.course_id " +
                     "WHERE a.medical_status != 'None' " +
                     "ORDER BY a.session_date DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapAttendance(rs));
            }
        } catch (SQLException e) {
            System.err.println("AttendanceDAO getAllMedicals error: " + e.getMessage());
        }
        return list;
    }

    public List<Attendance> getAttendanceForStudent(int studentId, Integer courseId) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.attendance_id, a.student_id, a.timetable_id, a.session_date, a.session_number, a.status, a.medical_status, a.reason, " +
                     "u.username AS student_reg_no, CONCAT(p.first_name, ' ', p.last_name) AS student_name, " +
                     "c.course_code, c.course_name, t.session_type " +
                     "FROM attendance a " +
                     "JOIN users u ON a.student_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "JOIN timetables t ON a.timetable_id = t.timetable_id " +
                     "JOIN courses c ON t.course_id = c.course_id " +
                     "WHERE a.student_id = ? " +
                     (courseId != null ? "AND c.course_id = ? " : "") +
                     "ORDER BY c.course_code, t.session_type, a.session_number";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            if (courseId != null) {
                stmt.setInt(2, courseId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAttendance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("AttendanceDAO getAttendanceForStudent error: " + e.getMessage());
        }
        return list;
    }

    public List<Attendance> getAttendanceByTimetableAndSession(int timetableId, int sessionNumber) {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.attendance_id, a.student_id, a.timetable_id, a.session_date, a.session_number, a.status, a.medical_status, a.reason, " +
                     "u.username AS student_reg_no, CONCAT(p.first_name, ' ', p.last_name) AS student_name, " +
                     "c.course_code, c.course_name, t.session_type " +
                     "FROM attendance a " +
                     "JOIN users u ON a.student_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "JOIN timetables t ON a.timetable_id = t.timetable_id " +
                     "JOIN courses c ON t.course_id = c.course_id " +
                     "WHERE a.timetable_id = ? AND a.session_number = ? " +
                     "ORDER BY u.username";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timetableId);
            stmt.setInt(2, sessionNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapAttendance(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("AttendanceDAO getAttendanceByTimetableAndSession error: " + e.getMessage());
        }
        return list;
    }

    private Attendance mapAttendance(ResultSet rs) throws SQLException {
        Attendance a = new Attendance();
        a.setAttendanceId(rs.getInt("attendance_id"));
        a.setStudentId(rs.getInt("student_id"));
        a.setStudentRegNo(rs.getString("student_reg_no"));
        a.setStudentName(rs.getString("student_name"));
        a.setTimetableId(rs.getInt("timetable_id"));
        a.setCourseCode(rs.getString("course_code"));
        a.setCourseName(rs.getString("course_name"));
        a.setSessionType(rs.getString("session_type"));
        a.setSessionDate(rs.getDate("session_date"));
        a.setSessionNumber(rs.getInt("session_number"));
        a.setStatus(rs.getString("status"));
        a.setMedicalStatus(rs.getString("medical_status"));
        a.setReason(rs.getString("reason"));
        return a;
    }
}
