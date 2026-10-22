package com.fot.tms.dao;

import com.fot.tms.database.DBConnection;
import com.fot.tms.model.Notice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDAO {

    public List<Notice> findAll() {
        List<Notice> list = new ArrayList<>();
        String sql = "SELECT n.notice_id, n.author_id, n.title, n.content, n.target_role, n.posted_date, " +
                     "CONCAT(p.first_name, ' ', p.last_name) AS author_name " +
                     "FROM notices n " +
                     "JOIN users u ON n.author_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "ORDER BY n.posted_date DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapNotice(rs));
            }
        } catch (SQLException e) {
            System.err.println("NoticeDAO findAll error: " + e.getMessage());
        }
        return list;
    }

    public List<Notice> findByTargetRole(String userRole) {
        List<Notice> list = new ArrayList<>();
        String sql = "SELECT n.notice_id, n.author_id, n.title, n.content, n.target_role, n.posted_date, " +
                     "CONCAT(p.first_name, ' ', p.last_name) AS author_name " +
                     "FROM notices n " +
                     "JOIN users u ON n.author_id = u.user_id " +
                     "LEFT JOIN profiles p ON u.user_id = p.user_id " +
                     "WHERE n.target_role = 'All' OR n.target_role = ? " +
                     "ORDER BY n.posted_date DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userRole);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapNotice(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("NoticeDAO findByTargetRole error: " + e.getMessage());
        }
        return list;
    }

    public boolean create(Notice notice) {
        String sql = "INSERT INTO notices (author_id, title, content, target_role, posted_date) VALUES (?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, notice.getAuthorId());
            stmt.setString(2, notice.getTitle());
            stmt.setString(3, notice.getContent());
            stmt.setString(4, notice.getTargetRole() != null ? notice.getTargetRole() : "All");
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        notice.setNoticeId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("NoticeDAO create error: " + e.getMessage());
        }
        return false;
    }

    public boolean update(Notice notice) {
        String sql = "UPDATE notices SET title = ?, content = ?, target_role = ? WHERE notice_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, notice.getTitle());
            stmt.setString(2, notice.getContent());
            stmt.setString(3, notice.getTargetRole());
            stmt.setInt(4, notice.getNoticeId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("NoticeDAO update error: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int noticeId) {
        String sql = "DELETE FROM notices WHERE notice_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, noticeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("NoticeDAO delete error: " + e.getMessage());
            return false;
        }
    }

    private Notice mapNotice(ResultSet rs) throws SQLException {
        Notice n = new Notice();
        n.setNoticeId(rs.getInt("notice_id"));
        n.setAuthorId(rs.getInt("author_id"));
        String aName = rs.getString("author_name");
        n.setAuthorName((aName != null && !aName.trim().isEmpty()) ? aName : "Administrator");
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));
        n.setTargetRole(rs.getString("target_role"));
        n.setPostedDate(rs.getTimestamp("posted_date"));
        return n;
    }
}
