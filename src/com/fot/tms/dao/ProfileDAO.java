package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.Profile;

import java.sql.*;




public class ProfileDAO {

    public Profile findByUserId(int userId) {
        String sql = "SELECT profile_id, user_id, first_name, last_name, contact_no, profile_picture_path FROM profiles WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Profile(
                        rs.getInt("profile_id"),
                        rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("contact_no"),
                        rs.getString("profile_picture_path")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("ProfileDAO findByUserId error: " + e.getMessage());
        }
        return null;
    }

    public boolean updateProfile(Profile profile) {
        String sql = "UPDATE profiles SET first_name = ?, last_name = ?, contact_no = ?, profile_picture_path = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, profile.getFirstName());
            stmt.setString(2, profile.getLastName());
            stmt.setString(3, profile.getContactNo());
            stmt.setString(4, profile.getProfilePicturePath());
            stmt.setInt(5, profile.getUserId());
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String insertSql = "INSERT INTO profiles (user_id, first_name, last_name, contact_no, profile_picture_path) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                    ins.setInt(1, profile.getUserId());
                    ins.setString(2, profile.getFirstName());
                    ins.setString(3, profile.getLastName());
                    ins.setString(4, profile.getContactNo());
                    ins.setString(5, profile.getProfilePicturePath());
                    return ins.executeUpdate() > 0;
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("ProfileDAO updateProfile error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateContactAndPhotoOnly(int userId, String contactNo, String photoPath) {
        String sql = "UPDATE profiles SET contact_no = ?, profile_picture_path = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contactNo);
            stmt.setString(2, photoPath);
            stmt.setInt(3, userId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                String insertSql = "INSERT INTO profiles (user_id, first_name, last_name, contact_no, profile_picture_path) VALUES (?, '', '', ?, ?)";
                try (PreparedStatement ins = conn.prepareStatement(insertSql)) {
                    ins.setInt(1, userId);
                    ins.setString(2, contactNo);
                    ins.setString(3, photoPath);
                    return ins.executeUpdate() > 0;
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("ProfileDAO updateContactAndPhotoOnly error: " + e.getMessage());
            return false;
        }
    }
}
