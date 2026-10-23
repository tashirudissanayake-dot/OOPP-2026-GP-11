package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.CourseMaterial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseMaterialDAO {

    public List<CourseMaterial> findByCourseId(int courseId) {
        List<CourseMaterial> list = new ArrayList<>();
        String sql = "SELECT m.material_id, m.course_id, m.title, m.file_path, m.uploaded_date, " +
                     "c.course_code, c.course_name " +
                     "FROM course_materials m " +
                     "JOIN courses c ON m.course_id = c.course_id " +
                     "WHERE m.course_id = ? " +
                     "ORDER BY m.uploaded_date DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapMaterial(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("CourseMaterialDAO findByCourseId error: " + e.getMessage());
        }
        return list;
    }

    public List<CourseMaterial> findAll() {
        List<CourseMaterial> list = new ArrayList<>();
        String sql = "SELECT m.material_id, m.course_id, m.title, m.file_path, m.uploaded_date, " +
                     "c.course_code, c.course_name " +
                     "FROM course_materials m " +
                     "JOIN courses c ON m.course_id = c.course_id " +
                     "ORDER BY c.course_code, m.uploaded_date DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapMaterial(rs));
            }
        } catch (SQLException e) {
            System.err.println("CourseMaterialDAO findAll error: " + e.getMessage());
        }
        return list;
    }

    public boolean create(CourseMaterial material) {
        String sql = "INSERT INTO course_materials (course_id, title, file_path, uploaded_date) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, material.getCourseId());
            stmt.setString(2, material.getTitle());
            stmt.setString(3, material.getFilePath());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        material.setMaterialId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("CourseMaterialDAO create error: " + e.getMessage());
        }
        return false;
    }

    public boolean update(CourseMaterial material) {
        String sql = "UPDATE course_materials SET course_id = ?, title = ?, file_path = ? WHERE material_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, material.getCourseId());
            stmt.setString(2, material.getTitle());
            stmt.setString(3, material.getFilePath());
            stmt.setInt(4, material.getMaterialId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CourseMaterialDAO update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int materialId) {
        String sql = "DELETE FROM course_materials WHERE material_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, materialId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CourseMaterialDAO delete error: " + e.getMessage());
            return false;
        }
    }

    private CourseMaterial mapMaterial(ResultSet rs) throws SQLException {
        CourseMaterial m = new CourseMaterial();
        m.setMaterialId(rs.getInt("material_id"));
        m.setCourseId(rs.getInt("course_id"));
        m.setTitle(rs.getString("title"));
        m.setFilePath(rs.getString("file_path"));
        m.setUploadedDate(rs.getTimestamp("uploaded_date"));
        m.setCourseCode(rs.getString("course_code"));
        m.setCourseName(rs.getString("course_name"));
        return m;
    }
}
