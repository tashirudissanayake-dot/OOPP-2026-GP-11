package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.Mark;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student Marks, Grades, and GPA.
 * Compliant with FR-17 to FR-23 and UGC Circular No. 12-2024.
 */
public class MarkDAO {

    public boolean recordOrUpdateMark(Mark mark) {
        String checkSql = "SELECT mark_id FROM marks WHERE student_id = ? AND course_id = ?";
        String updateSql = "UPDATE marks SET ca_quiz = ?, ca_assessment = ?, ca_mid = ?, ca_total = ?, " +
                           "final_exam_mark = ?, overall_mark = ?, letter_grade = ?, grade_point = ? " +
                           "WHERE student_id = ? AND course_id = ?";
        String insertSql = "INSERT INTO marks (student_id, course_id, ca_quiz, ca_assessment, ca_mid, ca_total, " +
                           "final_exam_mark, overall_mark, letter_grade, grade_point) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection()) {
            boolean exists = false;
            try (PreparedStatement check = conn.prepareStatement(checkSql)) {
                check.setInt(1, mark.getStudentId());
                check.setInt(2, mark.getCourseId());
                try (ResultSet rs = check.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                    }
                }
            }

            if (exists) {
                try (PreparedStatement upd = conn.prepareStatement(updateSql)) {
                    upd.setDouble(1, mark.getCaQuiz());
                    upd.setDouble(2, mark.getCaAssessment());
                    upd.setDouble(3, mark.getCaMid());
                    upd.setDouble(4, mark.getCaTotal());
                    upd.setDouble(5, mark.getFinalExamMark());
                    upd.setDouble(6, mark.getOverallMark());
                    upd.setString(7, mark.getLetterGrade());
                    upd.setDouble(8, mark.getGradePoint());
                    upd.setInt(9, mark.getStudentId());
                    upd.setInt(10, mark.getCourseId());
                    return upd.executeUpdate() > 0;
                }
            } else {
                try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                    ins.setInt(1, mark.getStudentId());
                    ins.setInt(2, mark.getCourseId());
                    ins.setDouble(3, mark.getCaQuiz());
                    ins.setDouble(4, mark.getCaAssessment());
                    ins.setDouble(5, mark.getCaMid());
                    ins.setDouble(6, mark.getCaTotal());
                    ins.setDouble(7, mark.getFinalExamMark());
                    ins.setDouble(8, mark.getOverallMark());
                    ins.setString(9, mark.getLetterGrade());
                    ins.setDouble(10, mark.getGradePoint());
                    return ins.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("MarkDAO recordOrUpdateMark error: " + e.getMessage());
            return false;
        }
    }

    public List<Mark> findByStudentId(int studentId) {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT m.mark_id, m.student_id, m.course_id, m.ca_quiz, m.ca_assessment, m.ca_mid, m.ca_total, " +
                     "m.final_exam_mark, m.overall_mark, m.letter_grade, m.grade_point, " +
                     "u.username AS student_reg_no, CONCAT(p.first_name, ' ', p.last_name) AS student_name, " +
                     "c.course_code, c.course_name, c.credits, c.semester " +
                     "FROM marks m " +
                     "JOIN users u ON m.student_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "JOIN courses c ON m.course_id = c.course_id " +
                     "WHERE m.student_id = ? " +
                     "ORDER BY c.semester, c.course_code";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMark(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("MarkDAO findByStudentId error: " + e.getMessage());
        }
        return list;
    }

    public List<Mark> findByCourseId(int courseId) {
        List<Mark> list = new ArrayList<>();
        // u.user_id AS real_student_id is always non-null even when no marks row exists (LEFT JOIN)
        String sql = "SELECT m.mark_id, u.user_id AS real_student_id, m.course_id, m.ca_quiz, m.ca_assessment, m.ca_mid, m.ca_total, " +
                     "m.final_exam_mark, m.overall_mark, m.letter_grade, m.grade_point, " +
                     "u.username AS student_reg_no, CONCAT(p.first_name, ' ', p.last_name) AS student_name, " +
                     "c.course_code, c.course_name, c.credits, c.semester " +
                     "FROM users u " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "CROSS JOIN courses c ON c.course_id = ? " +
                     "LEFT JOIN marks m ON m.student_id = u.user_id AND m.course_id = c.course_id " +
                     "WHERE u.role = 'Undergraduate' " +
                     "ORDER BY u.username";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mark m = new Mark();
                    m.setMarkId(rs.getInt("mark_id"));
                    m.setStudentId(rs.getInt("real_student_id")); // Always the actual user_id, never 0
                    m.setCourseId(courseId);
                    m.setStudentRegNo(rs.getString("student_reg_no"));
                    m.setStudentName(rs.getString("student_name"));
                    m.setCourseCode(rs.getString("course_code"));
                    m.setCourseName(rs.getString("course_name"));
                    m.setCredits(rs.getInt("credits"));
                    m.setSemester(rs.getInt("semester"));
                    m.setCaQuiz(rs.getDouble("ca_quiz"));
                    m.setCaAssessment(rs.getDouble("ca_assessment"));
                    m.setCaMid(rs.getDouble("ca_mid"));
                    m.setCaTotal(rs.getDouble("ca_total"));
                    m.setFinalExamMark(rs.getDouble("final_exam_mark"));
                    m.setOverallMark(rs.getDouble("overall_mark"));
                    String grade = rs.getString("letter_grade");
                    m.setLetterGrade(grade != null ? grade : "E");
                    m.setGradePoint(rs.getDouble("grade_point"));
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            System.err.println("MarkDAO findByCourseId error: " + e.getMessage());
        }
        return list;
    }

    public List<Mark> findAll() {
        List<Mark> list = new ArrayList<>();
        String sql = "SELECT m.mark_id, m.student_id, m.course_id, m.ca_quiz, m.ca_assessment, m.ca_mid, m.ca_total, " +
                     "m.final_exam_mark, m.overall_mark, m.letter_grade, m.grade_point, " +
                     "u.username AS student_reg_no, CONCAT(p.first_name, ' ', p.last_name) AS student_name, " +
                     "c.course_code, c.course_name, c.credits, c.semester " +
                     "FROM marks m " +
                     "JOIN users u ON m.student_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "JOIN courses c ON m.course_id = c.course_id " +
                     "ORDER BY u.username, c.course_code";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapMark(rs));
            }
        } catch (SQLException e) {
            System.err.println("MarkDAO findAll error: " + e.getMessage());
        }
        return list;
    }

    private Mark mapMark(ResultSet rs) throws SQLException {
        Mark m = new Mark();
        m.setMarkId(rs.getInt("mark_id"));
        m.setStudentId(rs.getInt("student_id"));
        m.setStudentRegNo(rs.getString("student_reg_no"));
        m.setStudentName(rs.getString("student_name"));
        m.setCourseId(rs.getInt("course_id"));
        m.setCourseCode(rs.getString("course_code"));
        m.setCourseName(rs.getString("course_name"));
        m.setCredits(rs.getInt("credits"));
        m.setSemester(rs.getInt("semester"));
        m.setCaQuiz(rs.getDouble("ca_quiz"));
        m.setCaAssessment(rs.getDouble("ca_assessment"));
        m.setCaMid(rs.getDouble("ca_mid"));
        m.setCaTotal(rs.getDouble("ca_total"));
        m.setFinalExamMark(rs.getDouble("final_exam_mark"));
        m.setOverallMark(rs.getDouble("overall_mark"));
        m.setLetterGrade(rs.getString("letter_grade"));
        m.setGradePoint(rs.getDouble("grade_point"));
        return m;
    }
}
