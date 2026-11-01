package com.fot.tms.model;





public class AdminUser extends User {

    public AdminUser() {
        super();
        setRole("Admin");
    }

    public AdminUser(int userId, String username, String passwordHash, String email, String status) {
        super(userId, username, passwordHash, "Admin", email, status);
    }

    @Override
    public boolean canChangeOwnPassword() {
        return true;
    }

    @Override
    public boolean canEditProfileNames() {
        return true;
    }

    @Override
    public String getRoleDisplayName() {
        return "System Administrator";
    }
}
