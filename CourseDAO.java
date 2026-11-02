package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public List<Course> findAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.course_id, c.coordinator_id, c.course_code, c.course_name, c.credits, c.semester, c.ca_weight, c.final_weight, " +
                     "CONCAT(p.first_name, ' ', p.last_name) AS coordinator_name " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.coordinator_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "ORDER BY c.semester, c.course_code";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course c = mapCourse(rs);
                list.add(c);
            }
        } catch (SQLException e) {
            System.err.println("CourseDAO findAll error: " + e.getMessage());
        }
        return list;
    }

    public Course findById(int courseId) {
        String sql = "SELECT c.course_id, c.coordinator_id, c.course_code, c.course_name, c.credits, c.semester, c.ca_weight, c.final_weight, " +
                     "CONCAT(p.first_name, ' ', p.last_name) AS coordinator_name " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.coordinator_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE c.course_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapCourse(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("CourseDAO findById error: " + e.getMessage());
        }
        return null;
    }

    public List<Course> findByCoordinatorId(int lecturerId) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.course_id, c.coordinator_id, c.course_code, c.course_name, c.credits, c.semester, c.ca_weight, c.final_weight, " +
                     "CONCAT(p.first_name, ' ', p.last_name) AS coordinator_name " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.coordinator_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE c.coordinator_id = ? " +
                     "ORDER BY c.course_code";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, lecturerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCourse(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("CourseDAO findByCoordinatorId error: " + e.getMessage());
        }
        return list;
    }

    public boolean create(Course course) {
        String sql = "INSERT INTO courses (coordinator_id, course_code, course_name, credits, semester, ca_weight, final_weight) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (course.getCoordinatorId() != null && course.getCoordinatorId() > 0) {
                stmt.setInt(1, course.getCoordinatorId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, course.getCourseCode());
            stmt.setString(3, course.getCourseName());
            stmt.setInt(4, course.getCredits());
            stmt.setInt(5, course.getSemester());
            stmt.setDouble(6, course.getCaWeight());
            stmt.setDouble(7, course.getFinalWeight());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        course.setCourseId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("CourseDAO create error: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Course course) {
        String sql = "UPDATE courses SET coordinator_id = ?, course_code = ?, course_name = ?, credits = ?, semester = ?, ca_weight = ?, final_weight = ? WHERE course_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (course.getCoordinatorId() != null && course.getCoordinatorId() > 0) {
                stmt.setInt(1, course.getCoordinatorId());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, course.getCourseCode());
            stmt.setString(3, course.getCourseName());
            stmt.setInt(4, course.getCredits());
            stmt.setInt(5, course.getSemester());
            stmt.setDouble(6, course.getCaWeight());
            stmt.setDouble(7, course.getFinalWeight());
            stmt.setInt(8, course.getCourseId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CourseDAO update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CourseDAO delete error: " + e.getMessage());
            return false;
        }
    }

    private Course mapCourse(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseId(rs.getInt("course_id"));
        int coordId = rs.getInt("coordinator_id");
        if (!rs.wasNull()) {
            c.setCoordinatorId(coordId);
        }
        c.setCoordinatorName(rs.getString("coordinator_name"));
        c.setCourseCode(rs.getString("course_code"));
        c.setCourseName(rs.getString("course_name"));
        c.setCredits(rs.getInt("credits"));
        c.setSemester(rs.getInt("semester"));
        c.setCaWeight(rs.getDouble("ca_weight"));
        c.setFinalWeight(rs.getDouble("final_weight"));
        return c;
    }
}
