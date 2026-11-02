package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.Profile;
import com.fot.tms.model.User;
import com.fot.tms.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;





public class UserDAO {

    public User authenticate(String username, String plainPassword) {
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.role, u.email, u.status, " +
                     "p.profile_id, p.first_name, p.last_name, p.contact_no, p.profile_picture_path " +
                     "FROM users u " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE u.username = ? AND u.status = 'Active'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (PasswordUtil.verifyPassword(plainPassword, storedHash)) {
                        return mapUserWithProfile(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO authenticate error: " + e.getMessage());
        }
        return null;
    }

    public User findById(int userId) {
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.role, u.email, u.status, " +
                     "p.profile_id, p.first_name, p.last_name, p.contact_no, p.profile_picture_path " +
                     "FROM users u " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE u.user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUserWithProfile(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO findById error: " + e.getMessage());
        }
        return null;
    }

    public User findByUsername(String username) {
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.role, u.email, u.status, " +
                     "p.profile_id, p.first_name, p.last_name, p.contact_no, p.profile_picture_path " +
                     "FROM users u " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE u.username = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUserWithProfile(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO findByUsername error: " + e.getMessage());
        }
        return null;
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.role, u.email, u.status, " +
                     "p.profile_id, p.first_name, p.last_name, p.contact_no, p.profile_picture_path " +
                     "FROM users u " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "ORDER BY u.role, u.username";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapUserWithProfile(rs));
            }
        } catch (SQLException e) {
            System.err.println("UserDAO findAll error: " + e.getMessage());
        }
        return list;
    }

    public List<User> findByRole(String role) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.role, u.email, u.status, " +
                     "p.profile_id, p.first_name, p.last_name, p.contact_no, p.profile_picture_path " +
                     "FROM users u " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE u.role = ? " +
                     "ORDER BY u.username";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapUserWithProfile(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("UserDAO findByRole error: " + e.getMessage());
        }
        return list;
    }

    public boolean create(User user, Profile profile, String plainPassword) {
        String insertUserSql = "INSERT INTO users (username, password_hash, role, email, status) VALUES (?, ?, ?, ?, ?)";
        String insertProfileSql = "INSERT INTO profiles (user_id, first_name, last_name, contact_no, profile_picture_path) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            int newUserId = 0;
            try (PreparedStatement stmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, PasswordUtil.hashPassword(plainPassword));
                stmt.setString(3, user.getRole());
                stmt.setString(4, user.getEmail());
                stmt.setString(5, user.getStatus() != null ? user.getStatus() : "Active");
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getInt(1);
                        user.setUserId(newUserId);
                    }
                }
            }

            if (newUserId > 0) {
                try (PreparedStatement pStmt = conn.prepareStatement(insertProfileSql)) {
                    pStmt.setInt(1, newUserId);
                    pStmt.setString(2, profile != null ? profile.getFirstName() : "");
                    pStmt.setString(3, profile != null ? profile.getLastName() : "");
                    pStmt.setString(4, profile != null ? profile.getContactNo() : "");
                    pStmt.setString(5, profile != null ? profile.getProfilePicturePath() : "");
                    pStmt.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("UserDAO create error: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) {}
            }
        }
    }

    public boolean update(User user, Profile profile) {
        String updateUserSql = "UPDATE users SET email = ?, status = ?, role = ? WHERE user_id = ?";
        String updateProfileSql = "UPDATE profiles SET first_name = ?, last_name = ?, contact_no = ?, profile_picture_path = ? WHERE user_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(updateUserSql)) {
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getStatus());
                stmt.setString(3, user.getRole());
                stmt.setInt(4, user.getUserId());
                stmt.executeUpdate();
            }

            if (profile != null) {
                try (PreparedStatement pStmt = conn.prepareStatement(updateProfileSql)) {
                    pStmt.setString(1, profile.getFirstName());
                    pStmt.setString(2, profile.getLastName());
                    pStmt.setString(3, profile.getContactNo());
                    pStmt.setString(4, profile.getProfilePicturePath());
                    pStmt.setInt(5, user.getUserId());
                    int updated = pStmt.executeUpdate();
                    if (updated == 0) {

                        String insertProf = "INSERT INTO profiles (user_id, first_name, last_name, contact_no, profile_picture_path) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement ins = conn.prepareStatement(insertProf)) {
                            ins.setInt(1, user.getUserId());
                            ins.setString(2, profile.getFirstName());
                            ins.setString(3, profile.getLastName());
                            ins.setString(4, profile.getContactNo());
                            ins.setString(5, profile.getProfilePicturePath());
                            ins.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("UserDAO update error: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) {}
            }
        }
    }

    public boolean updateStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO updateStatus error: " + e.getMessage());
            return false;
        }
    }

    public boolean resetPassword(int userId, String newPlainPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, PasswordUtil.hashPassword(newPlainPassword));
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO resetPassword error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO delete error: " + e.getMessage());
            return false;
        }
    }

    private User mapUserWithProfile(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String username = rs.getString("username");
        String passwordHash = rs.getString("password_hash");
        String role = rs.getString("role");
        String email = rs.getString("email");
        String status = rs.getString("status");

        User user = User.createPolymorphicUser(userId, username, passwordHash, role, email, status);

        int profId = rs.getInt("profile_id");
        if (!rs.wasNull()) {
            Profile profile = new Profile();
            profile.setProfileId(profId);
            profile.setUserId(user.getUserId());
            profile.setFirstName(rs.getString("first_name"));
            profile.setLastName(rs.getString("last_name"));
            profile.setContactNo(rs.getString("contact_no"));
            profile.setProfilePicturePath(rs.getString("profile_picture_path"));
            user.setProfile(profile);
        }
        return user;
    }
}
