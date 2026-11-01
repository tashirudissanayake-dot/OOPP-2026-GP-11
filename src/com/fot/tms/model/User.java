package com.fot.tms.model;

import java.io.Serializable;




public class User implements Serializable {
    private int userId;
    private String username;
    private String passwordHash;
    private String role; 
    private String email;
    private String status; 
    private Profile profile;

    public User() {}

    public User(int userId, String username, String passwordHash, String role, String email, String status) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.email = email;
        this.status = status;
    }

   
   
   
    public static User createPolymorphicUser(int userId, String username, String passwordHash, String role, String email, String status) {
        if ("Admin".equalsIgnoreCase(role)) {
            return new AdminUser(userId, username, passwordHash, email, status);
        } else if ("Lecturer".equalsIgnoreCase(role)) {
            return new LecturerUser(userId, username, passwordHash, email, status);
        } else if ("TO".equalsIgnoreCase(role)) {
            return new TechnicalOfficerUser(userId, username, passwordHash, email, status);
        } else if ("Undergraduate".equalsIgnoreCase(role)) {
            return new UndergraduateUser(userId, username, passwordHash, email, status);
        }
        return new User(userId, username, passwordHash, role, email, status);
    }

    public boolean canChangeOwnPassword() {
        return "Admin".equalsIgnoreCase(role);
    }

    public boolean canEditProfileNames() {
        return !"Undergraduate".equalsIgnoreCase(role);
    }

    public boolean canEditContactDetails() {
        return true;
    }

    public String getRoleDisplayName() {
        return role != null ? role : "System User";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getFullName() {
        if (profile != null) {
            return profile.getFullName();
        }
        return username;
    }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
