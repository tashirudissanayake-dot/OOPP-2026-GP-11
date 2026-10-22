package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.Timetable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    public List<Timetable> findAll() {
        List<Timetable> list = new ArrayList<>();
        String sql = "SELECT t.timetable_id, t.course_id, t.session_type, t.day_of_week, t.time_slot, t.venue, t.department, " +
                     "c.course_code, c.course_name " +
                     "FROM timetables t " +
                     "JOIN courses c ON t.course_id = c.course_id " +
                     "ORDER BY FIELD(t.day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), t.time_slot";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapTimetable(rs));
            }
        } catch (SQLException e) {
            System.err.println("TimetableDAO findAll error: " + e.getMessage());
        }
        return list;
    }

    public List<Timetable> findByDepartment(String department) {
        List<Timetable> list = new ArrayList<>();
        String sql = "SELECT t.timetable_id, t.course_id, t.session_type, t.day_of_week, t.time_slot, t.venue, t.department, " +
                     "c.course_code, c.course_name " +
                     "FROM timetables t " +
                     "JOIN courses c ON t.course_id = c.course_id " +
                     "WHERE t.department = ? OR ? = 'All' " +
                     "ORDER BY FIELD(t.day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'), t.time_slot";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, department);
            stmt.setString(2, department);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapTimetable(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("TimetableDAO findByDepartment error: " + e.getMessage());
        }
        return list;
    }

    public Timetable findById(int timetableId) {
        String sql = "SELECT t.timetable_id, t.course_id, t.session_type, t.day_of_week, t.time_slot, t.venue, t.department, " +
                     "c.course_code, c.course_name " +
                     "FROM timetables t " +
                     "JOIN courses c ON t.course_id = c.course_id " +
                     "WHERE t.timetable_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timetableId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapTimetable(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("TimetableDAO findById error: " + e.getMessage());
        }
        return null;
    }

    public boolean create(Timetable tt) {
        String sql = "INSERT INTO timetables (course_id, session_type, day_of_week, time_slot, venue, department) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, tt.getCourseId());
            stmt.setString(2, tt.getSessionType());
            stmt.setString(3, tt.getDayOfWeek());
            stmt.setString(4, tt.getTimeSlot());
            stmt.setString(5, tt.getVenue());
            stmt.setString(6, tt.getDepartment() != null ? tt.getDepartment() : "Information and Communication Technology");
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        tt.setTimetableId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("TimetableDAO create error: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Timetable tt) {
        String sql = "UPDATE timetables SET course_id = ?, session_type = ?, day_of_week = ?, time_slot = ?, venue = ?, department = ? WHERE timetable_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, tt.getCourseId());
            stmt.setString(2, tt.getSessionType());
            stmt.setString(3, tt.getDayOfWeek());
            stmt.setString(4, tt.getTimeSlot());
            stmt.setString(5, tt.getVenue());
            stmt.setString(6, tt.getDepartment());
            stmt.setInt(7, tt.getTimetableId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TimetableDAO update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int timetableId) {
        String sql = "DELETE FROM timetables WHERE timetable_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, timetableId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("TimetableDAO delete error: " + e.getMessage());
            return false;
        }
    }

    private Timetable mapTimetable(ResultSet rs) throws SQLException {
        Timetable t = new Timetable();
        t.setTimetableId(rs.getInt("timetable_id"));
        t.setCourseId(rs.getInt("course_id"));
        t.setCourseCode(rs.getString("course_code"));
        t.setCourseName(rs.getString("course_name"));
        t.setSessionType(rs.getString("session_type"));
        t.setDayOfWeek(rs.getString("day_of_week"));
        t.setTimeSlot(rs.getString("time_slot"));
        t.setVenue(rs.getString("venue"));
        t.setDepartment(rs.getString("department"));
        return t;
    }
}
